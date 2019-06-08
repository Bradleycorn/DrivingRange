package net.bradball.android.drivingrange.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import net.bradball.android.drivingrange.R

class ExplainPermissionsDialog(private val resultOK: ()->Unit): DialogFragment() {




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.explain_permissions)
                .setPositiveButton(
                    R.string.button_ok,
                    DialogInterface.OnClickListener { _, _ -> resultOK() })
//                .setNegativeButton(R.string.cancel,
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // User cancelled the dialog
//                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }
}