package com.example.e_commerce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.adapters.ProductAdapter
import com.example.e_commerce.databinding.FragmentProductsBinding
import com.example.e_commerce.models.ProductModel
import com.example.e_commerce.models.SubCategoryModel
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Products.newInstance] factory method to
 * create an instance of this fragment.
 */
class Products : Fragment(),CategoryInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentProductsBinding
    var productList=ArrayList<ProductModel>()
    lateinit var productAdapter: ProductAdapter
    var productModel = ProductModel()
    var subCategoryModel = SubCategoryModel()
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        db.collection("Product")
           // .whereEqualTo("catId", ProductModel.id)
            .addSnapshotListener { value, error ->
                if (value != null)
                    for (snapshots in value!!.documentChanges) {
                        when (snapshots.type) {
                            DocumentChange.Type.ADDED -> {
                                var productModel = ProductModel()
                                productModel = snapshots.document.toObject(productModel::class.java)
                                productModel.key = snapshots.document.id ?: ""
                                productList.add(productModel)
                                productAdapter.notifyDataSetChanged()
                            }
                            DocumentChange.Type.REMOVED -> {
                                var productModel = ProductModel()
                                productModel = snapshots.document.toObject(productModel::class.java)
                                productModel.key = snapshots.document.id ?: ""
                                for (i in 0..productList.size - 1) {
                                    if ((snapshots.document.id ?: "").equals(productList[i].key)) {
                                        productList.removeAt(i)
                                        break
                                    }
                                }
                                productAdapter.notifyDataSetChanged()
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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductsBinding.inflate(layoutInflater)

        productAdapter = ProductAdapter(productList, this)
        binding.rvList.adapter = productAdapter
        binding.rvList.layoutManager = LinearLayoutManager(context)


        binding.floatingBtn.setOnClickListener {
            findNavController().navigate(
                R.id.productsAdd,
                bundleOf("SubCategory" to productModel)
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
         * @return A new instance of fragment Products.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Products().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun edit(position: Int) {
        findNavController().navigate(R.id.productsAdd, bundleOf("SubCategory" to subCategoryModel,"Product" to productList, "isUpdate" to true))
    }

    override fun subCat(position: Int) {
        TODO("Not yet implemented")
    }
}