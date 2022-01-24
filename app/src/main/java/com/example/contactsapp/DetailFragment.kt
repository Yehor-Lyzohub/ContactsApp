package com.example.contactsapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.contactsapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val viewModel: ContactsViewModel by viewModels()

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    val args: DetailFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)

        viewModel.getUserInfo(args.contactId)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        viewModel.UserInfoLiveData.observe(viewLifecycleOwner, {
                newData ->
            binding.contactName.text = newData.firstName + " " + newData.lastName
            binding.contactNumber.text = newData.phone
            binding.contactImgLarge.loadImageLarge(DetailFragment(), newData.picture)
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    private fun ImageView.loadImageLarge(fragment: DetailFragment, url: String) {
        Glide.with(this)
            .load(url)
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.loading_animation)
            .into(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}