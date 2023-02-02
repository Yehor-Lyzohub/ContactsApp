package com.example.contactsapp.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactsapp.adapters.ContactsAdapter
import com.example.contactsapp.databinding.FragmentMainBinding
import com.example.contactsapp.viewmodels.ContactsViewModel

class MainFragment : Fragment() {

    private val viewModel: ContactsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this, ContactsViewModel.ContactsViewModelFactory(
                activity.application
            )
        )[ContactsViewModel::class.java]
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val args: DetailFragmentArgs by navArgs()

    private val adapter = ContactsAdapter { viewModel.addOrRemoveFavorite(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            contacts?.apply {
                adapter.setData(contacts)
            }
        }

        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            })

        onSearch(binding.searchItem)
    }

    private fun onSearch(searchItem: SearchView) {
        searchItem.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    if (view != null)
                        viewModel.contacts.observe(viewLifecycleOwner) { it ->
                            adapter.setData(it.filter {
                                it.firstName.lowercase().contains(newText!!)
                            })
                        }
                }, 2000)
                return true
            }
        })
    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetWorkErrorShown()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}