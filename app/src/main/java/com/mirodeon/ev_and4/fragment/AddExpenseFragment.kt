package com.mirodeon.ev_and4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import com.mirodeon.ev_and4.R
import com.mirodeon.ev_and4.activity.MainActivity
import com.mirodeon.ev_and4.app.MyApp
import com.mirodeon.ev_and4.databinding.FragmentAddExpenseBinding
import com.mirodeon.ev_and4.entity.Expense
import com.mirodeon.ev_and4.viewModel.ExpenseViewModel
import com.mirodeon.ev_and4.viewModel.ExpenseViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class AddExpenseFragment : Fragment() {
    private var binding: FragmentAddExpenseBinding? = null
    private val viewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(
            (activity?.application as MyApp).database.expenseDao()
        )
    }

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addActionToolbar()
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun addActionToolbar() {
        val main = (activity as MainActivity)
        main.binding?.toolbarMenu?.imageSaveAction?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.insertExpense(
                    Expense(
                        date = Date(2020, 8, 12),
                        name = "Test",
                        value = 66.6F,
                        typeExpenseId = 1
                    )
                )
            }
            main.navController.popBackStack(R.id.expenseListFragment, false)
        }
    }
}