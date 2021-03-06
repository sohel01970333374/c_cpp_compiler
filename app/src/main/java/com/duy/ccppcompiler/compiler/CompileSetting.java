/*
 * Copyright 2018 Mr Duy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duy.ccppcompiler.compiler;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.duy.ccppcompiler.R;
import com.duy.ccppcompiler.compiler.shell.ArgumentBuilder;

/**
 * Created by Duy on 17-May-18.
 */

public class CompileSetting implements ICompileSetting {
    private SharedPreferences mPref;
    private Context mContext;

    public CompileSetting(Context context) {
        mContext = context;
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public String getCFlags() {
        ArgumentBuilder builder = new ArgumentBuilder();
        builder.addFlags(getGCCFlags());

        String cFlags = mPref.getString(mContext.getString(R.string.pref_key_c_options), "");
        builder.addFlags(cFlags);

        return builder.build();
    }

    public void setCFlags(String cFlags) {
        mPref.edit().putString(mContext.getString(R.string.pref_cc_opts), cFlags).apply();
    }

    @Override
    public String getCxxFlags() {
        ArgumentBuilder builder = new ArgumentBuilder();
        builder.addFlags(getGCCFlags());

        String cxxFlags = mPref.getString(mContext.getString(R.string.pref_key_cxx_options), "");
        builder.addFlags(cxxFlags);

        return builder.build();

    }

    public void setCxxFlags(String cxxFlags) {
        mPref.edit().putString(mContext.getString(R.string.pref_cxx_opts), cxxFlags).apply();
    }

    @Override
    public String getLdFlags() {
        ArgumentBuilder builder = new ArgumentBuilder();
        String ldFlags = mPref.getString(mContext.getString(R.string.pref_key_linker_options), "");
        builder.addFlags(ldFlags);

        return builder.build();
    }

    public void setLinkerFlags(String ldFlags) {
        mPref.edit().putString(mContext.getString(R.string.pref_key_linker_options), ldFlags).apply();
    }

    @Override
    public String getMakeFlags() {
        return "";
    }

    private String getGCCFlags() {
        ArgumentBuilder builder = new ArgumentBuilder();

        //-ansi
        boolean ansi = mPref.getBoolean(mContext.getString(R.string.pref_c_options_ansi), false);
        builder.addFlags(ansi ? "-ansi" : "");

        //-fno-asm
        boolean noAsm = mPref.getBoolean(mContext.getString(R.string.pref_c_options_fno_asm), false);
        builder.addFlags(noAsm ? "-fno-asm" : "");

        //-traditional-cpp
        boolean traditionalCpp = mPref.getBoolean(mContext.getString(R.string.pref_c_options_traditional_cpp), false);
        builder.addFlags(traditionalCpp ? "-traditional-cpp" : "");

        //optimize
        String optimize = mPref.getString(mContext.getString(R.string.pref_option_optimization_level), "");
        if (!optimize.isEmpty()) {
            if (optimize.matches("(fast)|(g)|(s)|[0123]")) {
                builder.addFlags("-O" + optimize);
            }
        }

        //language standard
        String std = mPref.getString(mContext.getString(R.string.pref_option_language_standard), "");
        if (!std.isEmpty()) builder.addFlags("-std=" + std);

        //-w
        boolean w = mPref.getBoolean(mContext.getString(R.string.pref_option_w_warning), false);
        if (w) builder.addFlags("-w");

        //-wall
        boolean wall = mPref.getBoolean(mContext.getString(R.string.pref_option_wall_warning), false);
        if (wall) builder.addFlags("-Wall");

        //Wextra
        boolean wExtra = mPref.getBoolean(mContext.getString(R.string.pref_option_wextra_warning), false);
        if (wExtra) builder.addFlags("-Wextra");

        //Werror
        boolean Werror = mPref.getBoolean(mContext.getString(R.string.pref_option_werror), false);
        if (Werror) builder.addFlags("-Werror");

        return builder.build();
    }

}
