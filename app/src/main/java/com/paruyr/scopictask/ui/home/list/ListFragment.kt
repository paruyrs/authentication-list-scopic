package com.paruyr.scopictask.ui.home.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.paruyr.scopictask.R
import com.paruyr.scopictask.data.model.list.ItemData
import com.paruyr.scopictask.databinding.FragmentListBinding
import com.paruyr.scopictask.utils.Constants.DIALOG_ADDED_ITEM_KEY
import com.paruyr.scopictask.utils.Constants.DIALOG_ITEM_REQUEST_KEY
import com.paruyr.scopictask.viewmodel.ListViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val listViewModel: ListViewModel by viewModel()
    private val navController by lazy { findNavController() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        listViewModel.setup()

        // Set up the RecyclerView
        with(binding) {
            val layoutManager = LinearLayoutManager(context)
            list.layoutManager = LinearLayoutManager(context)
            val dividerItemDecoration = DividerItemDecoration(context, layoutManager.orientation)
            dividerItemDecoration.setDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.divider
                )!!
            )
            binding.list.addItemDecoration(dividerItemDecoration)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ItemRecyclerViewAdapter(mutableListOf(), object : ItemClickListener {
            override fun onItemRemove(item: ItemData) {
                listViewModel.removeItem(item)
            }
        })

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        binding.apply {
            list.adapter = adapter
            itemTouchHelper.attachToRecyclerView(list)

            profileButton.setOnClickListener {
                listViewModel.navigateToProfile()
            }

            addItemButton.setOnClickListener {
                listViewModel.addItem()
            }

            firestoreSwitch.setOnCheckedChangeListener { _, isChecked ->
                listViewModel.switchFirestoreState(isChecked)
            }
        }

        lifecycleScope.launch {
            listViewModel.navigation.collect { navigation ->
                when (navigation) {
                    ListViewModel.Navigation.Profile -> {
                        navController.navigate(R.id.action_listFragment_to_profileFragment)
                    }

                    ListViewModel.Navigation.AddListItem -> {
                        val inputDialog = InputDialogFragment()
                        inputDialog.show(parentFragmentManager, "InputDialogFragment")

                    }
                }
            }
        }

        lifecycleScope.launch {
            listViewModel.switchableItemsLiveData.collect { userItems ->
                adapter.updateItems(userItems.toMutableList())
            }
        }

        // Set up the result listener
        parentFragmentManager.setFragmentResultListener(
            DIALOG_ITEM_REQUEST_KEY,
            this
        ) { _, bundle ->
            val addedItem = bundle.getString(DIALOG_ADDED_ITEM_KEY)
            if (addedItem != null) {
                listViewModel.addItemToList(addedItem)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}