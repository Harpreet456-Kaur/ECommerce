package com.example.e_commerce.UpdatesItems

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.e_commerce.CategoryInterface
import com.example.e_commerce.MainActivity
import com.example.e_commerce.R
import com.example.e_commerce.adapters.CategoryAdapter
import com.example.e_commerce.databinding.FragmentAddItemsBinding
import com.example.e_commerce.models.CategoryModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddUpdateCategoryItemsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddUpdateCategoryItemsFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var  binding: FragmentAddItemsBinding?=null
    var storageRef = FirebaseStorage.getInstance()
    var categoryList = ArrayList<CategoryModel>()
    lateinit var categoryAdapter: CategoryAdapter
    lateinit var newInterface: CategoryInterface
    lateinit var mainActivity: MainActivity
    var categoryModel = CategoryModel()
    private var btmap : Bitmap? = null
    private var imageUri : Uri?=null
    val db = Firebase.firestore
    var isUpdate = false

    var getPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted->
        if(isGranted){
            Toast.makeText(requireActivity(), "Granted", Toast.LENGTH_SHORT).show()
            getImage.launch("image/*")
        }else{
            Toast.makeText(requireActivity(),"Not Granted", Toast.LENGTH_SHORT).show()
        }
    }

    var getImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        System.out.println("it $it")
        binding?.iv?.setImageURI(it)
        it?.let {
            btmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
            binding?.iv?.setImageBitmap(btmap)
            imageUri = it
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivity = activity as MainActivity

        arguments?.let {
            categoryModel = it.getSerializable("Category") as CategoryModel
            isUpdate = it.getBoolean("isUpdate", false)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): LinearLayout? {
        // Inflate the layout for this fragment
        binding = FragmentAddItemsBinding.inflate(layoutInflater)
//        binding?.btnDelete?.setOnClickListener {
//            db.collection("Category").document().delete()
//            findNavController().navigate(R.id.categories)
//        }

        binding?.iv?.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireActivity(),
                    READ_EXTERNAL_STORAGE
                )== PackageManager.PERMISSION_GRANTED)
            {
                getImage.launch("image/*")
                Toast.makeText(requireActivity(),"Granted",Toast.LENGTH_LONG).show()
            }
            else
                getPermission.launch(READ_EXTERNAL_STORAGE)
        }
        if(isUpdate == true){
            binding?.btnAdd?.setText("Update")
            binding?.btnDelete?.visibility = View.VISIBLE
            binding?.etName?.setText((categoryModel.name?:""))
            binding?.iv?.let {
                Glide.with(requireActivity())
                    .load(categoryModel.image)
                    .circleCrop()
                    .into(it)
            }
        }else{
            binding?.btnAdd?.setText("Add")
            binding?.btnDelete?.visibility = View.GONE
        }
        binding?.btnDelete?.setOnClickListener {
            db.collection("Category").document(categoryModel.key?:"").delete()
                .addOnSuccessListener {
                    findNavController().popBackStack()
                Toast.makeText(requireActivity(), "ADD", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {

                }
        }

        binding?.btnAdd?.setOnClickListener {
            if (binding?.etName?.text?.isEmpty() == true) {
                binding?.etName?.error = "Enter Category"
            } else {
//                System.out.println(" isUpdate $isUpdate")
//                if(isUpdate == false) {
//                    val categoryModel = CategoryModel(name = binding?.etName?.text.toString())
//                    db.collection("Category")
//                        .add(categoryModel)
//                        .addOnSuccessListener {
//                            Toast.makeText(requireActivity(), "ADD", Toast.LENGTH_SHORT).show()
//                        }.addOnFailureListener {
//                            Log.e("TAG", "Failure ${it}")
//                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                }
//                else{
//                    categoryModel.name = binding?.etName?.text.toString()
//                    db.collection("Category").document(categoryModel.key?:"")
//                        .set(categoryModel)
//                        .addOnSuccessListener {
//                            Toast.makeText(requireActivity(), "Data updated", Toast.LENGTH_SHORT).show()
//                        }.addOnFailureListener {
//                            Log.e("TAG", "Failure ${it}")
//                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                }
//                findNavController().navigate(R.id.categories)
//            }
//        }
                categoryModel.name = binding?.etName?.text.toString()
                System.out.println("this::imageUri ${imageUri}")
                if (imageUri != null) {
                    binding?.llLoader?.visibility = View.VISIBLE
                    val ref =
                        storageRef.reference.child(Calendar.getInstance().timeInMillis.toString())
                    var uploadTask = imageUri?.let { it1 -> ref.putFile(it1) }
                    uploadTask?.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                binding?.llLoader?.visibility = View.GONE
                                throw it
                            }
                        }
                        ref.downloadUrl
                    }?.addOnCompleteListener { task ->
                        //  binding?.llLoader?.visibility = View.GONE

                        System.out.println("in on complete listener")
                        if (task.isSuccessful) {
                            val downloadUri = task.result
                            categoryModel.image = downloadUri.toString()
                            AddUpdateCategory()
                        }
                    }
                } else {
                    AddUpdateCategory()
                }
            }
        }
        return binding?.root
    }

    fun AddUpdateCategory(){
        if(isUpdate){
            db.collection("Category").document(categoryModel.key?:"").set(categoryModel)
                .addOnSuccessListener {
                    binding?.llLoader?.visibility = View.GONE
                    //   mainActivity.navController.popBackStack()
                }.addOnFailureListener {

                }
        }else{
            db.collection("Category").add(categoryModel)
                .addOnSuccessListener {
                    binding?.llLoader?.visibility = View.GONE
                    // mainActivity.navController.popBackStack()
                }.addOnFailureListener {

                }
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddItems.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddUpdateCategoryItemsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
