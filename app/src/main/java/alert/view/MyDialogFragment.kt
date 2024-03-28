package alert.view

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.LottieAnimationView
import com.example.weatherapp.R

class MyDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.dialog_fragment_layout, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val window = dialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(
            requireActivity()
        )
        builder.setView(requireActivity().layoutInflater.inflate(R.layout.dialog_fragment_layout, null))
            .setPositiveButton("Save", DialogInterface.OnClickListener { dialog, id ->


            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->

                dialog.dismiss()
            })
        return builder.create()
    }
}
