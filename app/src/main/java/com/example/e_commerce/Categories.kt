package com.example.e_commerce

import android.icu.text.Transliterator.Position
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.databinding.FragmentCategoriesBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.Key

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Categories.newInstance] factory method to
 * create an instance of this fragment.
 */
class Categories : Fragment(), NewInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentCategoriesBinding
    var categoryList=ArrayList<CategoryModel>()
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var newInterface: NewInterface
    var categoryModel = CategoryModel()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        db.collection("Category").addSnapshotListener { value, error ->
            if (value != null)
                for (snapshots in value!!.documentChanges) {
                    when (snapshots.type) {
                        DocumentChange.Type.ADDED -> {
                            var categoryModel = CategoryModel()
                            categoryModel = snapshots.document.toObject(categoryModel::class.java)
                            categoryModel.key = snapshots.document.id ?: ""
                            categoryList.add(categoryModel)
                            categoryAdapter.notifyDataSetChanged()
                        }
                        DocumentChange.Type.REMOVED -> {
                            var categoryModel = CategoryModel()
                            categoryModel = snapshots.document.toObject(categoryModel::class.java)
                            categoryModel.key = snapshots.document.id ?: ""
                            for (i in 0..categoryList.size - 1) {
                                if ((snapshots.document.id ?: "").equals(categoryList[i].key)) {
                                    categoryList.removeAt(i)
                                    break
                                }
                            }
                            categoryAdapter.notifyDataSetChanged()
                        }
                        else -> {}
                    }
                }
            binding.rvList.clearFocus()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): RelativeLayout {
        // Inflate the layout for this fragment
        binding = FragmentCategoriesBinding.inflate(layoutInflater)

        categoryAdapter = CategoryAdapter(categoryList, this)
        binding.rvList.adapter = categoryAdapter
        binding.rvList.layoutManager = LinearLayoutManager(context)


        binding.floatingBtn.setOnClickListener {
            findNavController().navigate(R.id.addItems)
        }
        binding.plainSubCategory.setOnClickListener {
            findNavController().navigate(R.id.subCategories)
        }
        binding.PrintedSubCategory.setOnClickListener {
            findNavController().navigate(R.id.printedSubCategory)
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Categories.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param2: String) =
            Categories().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun edit(position: Int) {
        findNavController().navigate(R.id.addItems, bundleOf("Category" to categoryList[position], "isUpdate" to true))
    }

}