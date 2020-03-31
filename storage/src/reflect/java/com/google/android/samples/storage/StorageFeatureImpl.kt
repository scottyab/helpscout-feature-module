/*
 * Copyright 2019 Google LLC
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
 *
 */
package com.google.android.samples.storage

import android.content.Context
import android.preference.PreferenceManager
import com.google.android.samples.dynamiccodeloading.Logger
import com.google.android.samples.dynamiccodeloading.StorageFeature

const val PREF_COUNTER = "COUNTER"

class StorageFeatureImpl(private val context: Context, private val logger: Logger) : StorageFeature {
    private val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    override fun saveCounter(counter: Int) {
        sharedPrefs.edit().putInt(PREF_COUNTER, counter).apply()
        logger.log("Saved $counter to storage")

        val beaconId = "******-****-****-****-****"
        Beacon.Builder()
            .withBeaconId(beaconId)
            .withLogsEnabled(true)
            .build()
        BeaconActivity.open(context)

    }

    override fun loadCounter(): Int {
        return sharedPrefs.getInt(PREF_COUNTER, 0).also {
            logger.log("Loaded $it from storage")
        }
    }

    /**
     * The provider singleton. It is accessed from the base app ViewModel through reflection.
     */
    companion object Provider : StorageFeature.Provider {
        override fun get(dependencies: StorageFeature.Dependencies): StorageFeature {

            return StorageFeatureImpl(dependencies.getContext(), dependencies.getLogger())
        }
    }
}
