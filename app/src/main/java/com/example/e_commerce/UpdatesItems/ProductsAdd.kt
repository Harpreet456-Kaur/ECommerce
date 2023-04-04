package com.example.e_commerce.UpdatesItems

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.CategoryInterface
import com.example.e_commerce.adapters.ProductAdapter
import com.example.e_commerce.databinding.FragmentProductsAddBinding
import com.example.e_commerce.models.ProductModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [ProductsAdd.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductsAdd : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentProductsAddBinding
    var productList = ArrayList<ProductModel>()
    lateinit var productAdapter: ProductAdapter
    lateinit var categoryInterface: CategoryInterface
    var productModel = ProductModel()
    val db = Firebase.firestore
    var isUpdate = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductsAddBinding.inflate(layoutInflater)

        binding.btnAdd.setOnClickListener {
            if (binding.etName.text.isEmpty()) {
                binding.etName.error = "Enter Product"
            } else if (binding.etPrice.text.isEmpty()) {
                binding.etPrice.error = "Enter Price"
            } else if (binding.etDescription.text.isEmpty()) {
                binding.etDescription.error = "Enter Description"
            } else {
                System.out.println(" isUpdate $isUpdate")
                if (isUpdate == false) {
                    val productModel = ProductModel(
                        name = binding.etName.text.toString(),
                        price = binding.etPrice.text.toString(),
                        description = binding.etDescription.text.toString(),
                        catId = productModel.key,
                        subCatId = productModel.key
                    )
                    db.collection("Product")
                        .add(productModel)
                        .addOnSuccessListener {
                            Toast.makeText(requireActivity(), "ADD", Toast.LENGTH_SHORT).show()
                            findNavController().popBackStack()

                        }.addOnFailureListener {
                            Log.e("TAG", "Failure ${it}")
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
                                .show()
                        }

                } else {
                    val productModel = ProductModel(
                        name = binding.etName.text.toString(),
                        price = binding.etPrice.text.toString(),
                        description = binding.etDescription.text.toString(),
                        catId = productModel.key,
                        subCatId = productModel.key
                    )
                    db.collection("Product").document()
                        .set(productModel)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireActivity(),
                                "Data updated",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        }.addOnFailureListener {
                            Log.e("TAG", "Failure ${it}")
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
                                .show()
                        }
                }

            }
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
         * @return A new instance of fragment ProductsAdd.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductsAdd().apply {
                arguments = Bundle().apply {

                }
            }
    }
}