package com.example.arfurnitureapp

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode

class ARActivity : AppCompatActivity() {
    private lateinit var arFragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            val anchor = hitResult.createAnchor()

            ModelRenderable.builder()
                .setSource(this, Uri.parse("chair.sfb"))  // Must be in assets folder
                .build()
                .thenAccept { modelRenderable ->
                    val anchorNode = AnchorNode(anchor)
                    anchorNode.setParent(arFragment.arSceneView.scene)

                    val model = TransformableNode(arFragment.transformationSystem)
                    model.setParent(anchorNode)
                    model.renderable = modelRenderable
                    model.select()
                }
                .exceptionally {
                    Toast.makeText(this, "Model failed to load", Toast.LENGTH_SHORT).show()
                    null
                }
        }
    }
}
