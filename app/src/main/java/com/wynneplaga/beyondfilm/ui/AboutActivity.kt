package com.wynneplaga.beyondfilm.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.danielstone.materialaboutlibrary.MaterialAboutActivity
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.wynneplaga.beyondfilm.BuildConfig
import com.wynneplaga.beyondfilm.R


class AboutActivity : MaterialAboutActivity() {

    override fun getMaterialAboutList(context: Context): MaterialAboutList {
        return MaterialAboutList.Builder()
            .addCard(
                MaterialAboutCard.Builder()
                    .addItem(
                        MaterialAboutTitleItem.Builder()
                            .text(R.string.app_name)
                            .icon(R.mipmap.ic_launcher)
                            .build()
                    )
                    .addItem(
                        MaterialAboutActionItem.Builder().text("Version")
                            .icon(R.drawable.information_outline)
                            .subText(BuildConfig.VERSION_NAME)
                            .build()
                    )
                    .addItem(
                        MaterialAboutActionItem.Builder().text("Author")
                            .icon(R.drawable.person)
                            .subText("Wynne Plaga")
                            .setOnClickAction {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://github.com/turing-tech")
                                )
                                startActivity(intent)
                            }
                            .build()
                    )
                    .addItem(
                        MaterialAboutActionItem.Builder().text("Licenses")
                            .icon(R.drawable.information_outline)
                            .setOnClickAction {
                                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                            }
                            .build()
                    )
                    .build()
            )
            .build()
    }

    override fun getActivityTitle(): CharSequence? {
        return getString(R.string.about)
    }
}