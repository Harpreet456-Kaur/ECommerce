package com.example.e_commerce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.adapters.SubCategoryAdapter
import com.example.e_commerce.databinding.FragmentCategoriesBinding
import com.example.e_commerce.models.CategoryModel
import com.example.e_commerce.models.SubCategoryModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [SubCategories.newInstance] factory method to
 * create an instance of this fragment.
 */
class SubCategories : Fragment(), CategoryInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentCategoriesBinding
    var categoryList=ArrayList<SubCategoryModel>()
    lateinit var categoryAdapter: SubCategoryAdapter
    var categoryModel = CategoryModel()
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryModel = it.getSerializable("Category") as CategoryModel
        }
        db.collection("SubCategory")
            .whereEqualTo("catId", categoryModel.key)
            .addSnapshotListener { value, error ->
            if (value != null)
                for (snapshots in value!!.documentChanges) {
                    when (snapshots.type) {
                        DocumentChange.Type.ADDED -> {
                            var categoryModel = SubCategoryModel()
                            categoryModel = snapshots.document.toObject(SubCategoryModel::class.java)
                            categoryModel.id = snapshots.document.id ?: ""
                            categoryList.add(categoryModel)
                            categoryAdapter.notifyDataSetChanged()
                        }
                        DocumentChange.Type.REMOVED -> {
                            var categoryModel = SubCategoryModel()
                            categoryModel = snapshots.document.toObject(categoryModel::class.java)
                            categoryModel.id = snapshots.document.id ?: ""
                            for (i in 0..categoryList.size - 1) {
                                if ((snapshots.document.id ?: "").equals(categoryList[i].id)) {
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

        categoryAdapter = SubCategoryAdapter(categoryList, this)
        binding.rvList.adapter = categoryAdapter
        binding.rvList.layoutManager = LinearLayoutManager(context)


        binding.floatingBtn.setOnClickListener {
            findNavController().navigate(
                R.id.addUpdateSubCategoryItemsFragment,
                bundleOf("Category" to categoryModel)
            )
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
            SubCategories().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun edit(position: Int) {
        findNavController().navigate(
            R.id.addUpdateSubCategoryItemsFragment,
            bundleOf("Category" to categoryModel,"SubCategory" to categoryList, "isUpdate" to true)
        )
    }

    override fun subCat(position: Int) {


    }

}