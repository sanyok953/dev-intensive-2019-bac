package ru.skillbranch.devintensive.ui.profile

import android.content.res.Configuration
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.App
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.extensions.afterTextChanged
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.ui.custom.AvatarImageView
import ru.skillbranch.devintensive.ui.custom.TextDrawable
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel

class ProfileActivity : AppCompatActivity() {
    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>
    var isCorrectRepo = true


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewsModel()

        Log.d("M_ProfileActivity", "onCreate")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewsModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
    }

    private fun updateTheme(mode: Int) {
        Log.d("M_ProfileActivity", "updateTheme")
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {

        //Log.d("M_ProfileActivity", getInitials(profile.firstName, profile.lastName))


        val initials = Utils.toInitials(profile.firstName, profile.lastName)
        if (initials != null) {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val color = if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                resources.getColor(R.color.color_accent_night, theme)
            } else {
                resources.getColor(R.color.color_accent, theme)
            }
            /*val drawable = TextDrawable.builder()
                .buildRound(initials, color)*/
            val drawable = BitmapDrawable(resources, Utils.generateAvatar(App.applicationContext(), 112, initials, col = color))
            iv_avatar.setImageDrawable(drawable)
            iv_avatar.setupBitmap()
        } else {
            iv_avatar.setImageDrawable(R.drawable.avatar_default.toDrawable())
            iv_avatar.setupBitmap()
        }

        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {

        Log.d("M_ProfileActivity", getInitials(et_first_name.text.toString(), et_last_name.text.toString()))

        /*val initials = getInitials(et_first_name.text.toString(), et_last_name.text.toString())
        if (initials != "") {
            val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            val color = if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
                resources.getColor(R.color.color_accent_night, theme)
            } else {
                resources.getColor(R.color.color_accent, theme)
            }
            val drawable = TextDrawable.builder()
                .buildRound(initials, color)
            iv_avatar.setImageDrawable(drawable)
            iv_avatar.setupBitmap()
        } else {
            iv_avatar.setImageDrawable(R.drawable.avatar_default.toDrawable())
            iv_avatar.setupBitmap()
        }*/

        viewFields = mapOf(
            "nickName" to tv_nick_name,
            "rank" to tv_rank,
            "firstName" to et_first_name,
            "lastName" to et_last_name,
            "about" to et_about,
            "repository" to et_repository,
            "rating" to tv_rating,
            "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener{
            if (isEditMode) saveProfileInfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }

        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }

        et_repository.afterTextChanged {
            isCorrectRepo = true
            if (!Utils.validateRepoName(it)) {
                wr_repository.error = "Невалидный адрес репозитория"
                isCorrectRepo = false
            } else{
                wr_repository.error = ""
            }

        }
    }

    private fun getInitials(fn: String, ln: String): String {
        return Utils.toInitials(fn, ln) ?: ""
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter{ setOf("firstName","lastName","about","repository").contains(it.key) }
        for ((_,v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                    resources.getColor(R.color.color_accent, theme),
                    PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }

            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileInfo() {
        Profile(
            firstName = et_first_name.text.toString(),
            lastName = et_last_name.text.toString(),
            about = et_about.text.toString(),
            repository = et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

}
