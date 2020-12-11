package com.wynneplaga.beyondfilm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

typealias ResultBlock = (resultCode: Int, data: Intent?) -> Unit

open class BeyondFilmActivity: AppCompatActivity() {

    private val resultBlocks = ArrayList<ResultBlock>()

    protected fun listenForActivityResult(block: ResultBlock): Int {
        resultBlocks.add(block)
        return resultBlocks.size - 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode in 0..resultBlocks.size)
            resultBlocks[requestCode].invoke(resultCode, data)
    }

}