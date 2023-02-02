package com.example.contactsapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.contactsapp.viewmodels.ContactsViewModel
import com.example.contactsapp.databinding.FragmentDetailBinding
import com.example.contactsapp.loadSquaredImageWithGlide

class DetailFragment : Fragment() {

    private val viewModel: ContactsViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, ContactsViewModel.ContactsViewModelFactory(activity.application))
            .get(ContactsViewModel::class.java)
    }

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getUserDetails(args.contactId)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.userInfoLiveData.observe(viewLifecycleOwner) {
            binding.contactName.text = it.firstName + " " + it.lastName
            binding.contactNumber.text = it.phone
            binding.contactImgLarge.loadSquaredImageWithGlide(it.picture)
        }

        binding.favoriteCheckBoxOnDetails.isChecked = args.contactIsFavorite

        binding.favoriteCheckBoxOnDetails.setOnClickListener {
            viewModel.addOrRemoveFavorite(args.contactId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}