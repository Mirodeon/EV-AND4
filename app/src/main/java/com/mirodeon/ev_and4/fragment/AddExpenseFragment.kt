package com.mirodeon.ev_and4.fragment

import android.app.AlertDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.mirodeon.ev_and4.R
import com.mirodeon.ev_and4.activity.MainActivity
import com.mirodeon.ev_and4.app.MyApp
import com.mirodeon.ev_and4.databinding.FragmentAddExpenseBinding
import com.mirodeon.ev_and4.entity.Expense
import com.mirodeon.ev_and4.entity.ExpenseType
import com.mirodeon.ev_and4.viewModel.ExpenseViewModel
import com.mirodeon.ev_and4.viewModel.ExpenseViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class AddExpenseFragment : Fragment() {
    private var binding: FragmentAddExpenseBinding? = null
    private val viewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(
            MyApp.instance.database.expenseDao()
        )
    }
    private val expenseTypes = mutableListOf<ExpenseType>()
    private var selectedType: ExpenseType? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private var selectedDate: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addActionToolbar()
        addBtnType()
        setPicker()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addActionToolbar() {
        val main = (activity as MainActivity)
        main.binding?.toolbarMenu?.imageSaveAction?.setOnClickListener {
            val name = binding?.nameText?.text.toString()
            val amount = binding?.amountText?.text.toString().toFloatOrNull()
            val typeExpenseId = selectedType?.typeId
            if (name.isNotEmpty() && name.isNotBlank() && amount != null && typeExpenseId != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertExpense(
                        Expense(
                            date = selectedDate,
                            name = name,
                            value = amount,
                            typeExpenseId = typeExpenseId
                        )
                    )
                }
                main.navController.popBackStack(R.id.expenseListFragment, false)
            } else {
                Toast.makeText(activity, "Invalid Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addBtnType() {
        CoroutineScope(Dispatchers.IO).launch {
            expenseTypes.clear()
            expenseTypes.addAll(MyApp.instance.database.expenseTypeDao().getAll())
        }

        binding?.buttonAddType?.setOnClickListener {
            val mBuilder = AlertDialog.Builder(activity)
            mBuilder.setTitle("Choose an type")
            mBuilder.setSingleChoiceItems(
                expenseTypes.map { it.name }.toTypedArray(),
                -1
            ) { dialogInterface, i ->
                binding?.typeText?.text = expenseTypes[i].name
                selectedType = expenseTypes[i]
                dialogInterface.dismiss()
            }

            mBuilder.setNeutralButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            val mDialog = mBuilder.create()
            mDialog.show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setPicker() {
        val today = Calendar.getInstance()
        binding?.datePicker?.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val str = String.format("%d-%02d-%02d", year, month + 1, day)
            selectedDate = LocalDate.parse(str)
        }
    }
}