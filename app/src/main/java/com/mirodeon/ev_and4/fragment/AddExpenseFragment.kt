package com.mirodeon.ev_and4.fragment

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val amount = binding?.amountText?.text.toString().toFloat()
            val typeExpenseId = selectedType?.typeId
            val date = LocalDate.parse("2018-12-02")
            if (name.isNotEmpty() && name.isNotBlank() && !amount.isNaN() && typeExpenseId != null && date != null)
                CoroutineScope(Dispatchers.IO).launch {
                    typeExpenseId?.let { typeId ->
                        viewModel.insertExpense(
                            Expense(
                                date = date,
                                name = name,
                                value = amount,
                                typeExpenseId = typeId
                            )
                        )
                    }
                }
            main.navController.popBackStack(R.id.expenseListFragment, false)
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
}