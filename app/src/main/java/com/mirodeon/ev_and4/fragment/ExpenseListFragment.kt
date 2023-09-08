package com.mirodeon.ev_and4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mirodeon.ev_and4.adapter.ExpenseAdapter
import com.mirodeon.ev_and4.databinding.FragmentExpenseListBinding
import com.mirodeon.ev_and4.viewModel.ExpenseViewModel
import com.mirodeon.ev_and4.viewModel.ExpenseViewModelFactory
import com.mirodeon.ev_and4.app.MyApp
import kotlinx.coroutines.launch

class ExpenseListFragment : Fragment() {
    private var binding: FragmentExpenseListBinding? = null
    private var recyclerView: RecyclerView? = null
    private val viewModel: ExpenseViewModel by activityViewModels {
        ExpenseViewModelFactory(
            MyApp.instance.database.expenseDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding?.containerRecycler
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        val expenseAdapter = ExpenseAdapter {}
        recyclerView?.adapter = expenseAdapter
        lifecycle.coroutineScope.launch {
            viewModel.fullExpense().collect() {
                expenseAdapter.submitList(it)
            }
        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}