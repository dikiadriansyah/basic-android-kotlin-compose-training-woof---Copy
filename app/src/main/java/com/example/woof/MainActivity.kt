/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.woof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.animateColorAsState

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.Icons

import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

import com.example.woof.data.Dog
import com.example.woof.data.dogs
import com.example.woof.ui.theme.WoofTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WoofTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    WoofApp()
                }
            }
        }
    }
}

// Menambahkan gambar dan teks ke panel atas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(modifier: Modifier = Modifier){
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically){
//              verticalAlignment = Alignment.CenterVertically untuk ikon dan teks agar sejajar secara vertikal

                Image(
                   modifier= Modifier
                       .size(dimensionResource(id = R.dimen.image_size))
                       .padding(dimensionResource(id = R.dimen.padding_small)),
                    painter = painterResource(R.drawable.ic_woof_logo),
                    contentDescription = null
                )
                Text(text = stringResource(R.string.app_name), style = MaterialTheme.typography.displayLarge)
            }
        }
    )
}

/**
 * Composable that displays an app bar and a list of dogs.
 */
@Composable
fun WoofApp() {
    Scaffold(topBar = {
        WoofTopAppBar()
    }){ it->
//       Scaffold mendukung parameter contentWindowInsets yang dapat membantu menentukan inset untuk konten scaffold.
// WindowInsets adalah bagian layar tempat aplikasi Anda dapat berpotongan dengan UI sistem, yang
// ini akan diteruskan ke slot konten melalui parameter PaddingValues
//Nilai contentWindowInsets diteruskan ke LazyColumn sebagai contentPadding.
        LazyColumn(contentPadding = it) {

//        LazyColumn yang menampilkan dogItem
            items(dogs) {
                DogItem(dog = it,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
//        dimensionResource() untuk menambahkan nilai dari file dimensi.xml
            }
        }
    }
    /*
    Scaffold adalah tata letak yang menyediakan slot untuk berbagai komponen dan elemen layar,
    seperti Image, Row, atau Column. Scaffold juga menyediakan slot untuk TopAppBar
     */


}



/**
 * Composable that displays a list item containing a dog icon and their information.
 *
 * @param dog contains the data that populates the list item
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogItem(
    dog: Dog,
    modifier: Modifier = Modifier
) {

    var expanded by remember{ mutableStateOf(false)}
    /*
    Gunakan fungsi mutableStateOf() agar Compose mengamati perubahan apa pun pada nilai status, dan
    memicu rekomposisi untuk mengupdate UI. Gabungkan panggilan fungsi mutableStateOf() dengan
    fungsi remember() untuk menyimpan nilai dalam Komposisi selama komposisi awal, dan nilai yang
    disimpan akan ditampilkan selama rekomposisi.
     */

// (opsional) eksperimen dengan animasi lain
    val color by animateColorAsState(
        targetValue = if(expanded){
            MaterialTheme.colorScheme.tertiaryContainer
        }else{
            MaterialTheme.colorScheme.primaryContainer
        }
//        Jika item daftar diluaskan, tetapkan item daftar ke warna tertiaryContainer. Selain itu,
    //        tetapkan ke warna primaryContainer.
    )



//    turunan pertama DogItem()
    Card(modifier = modifier){
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        )
//                 Setel ke animasi pegas dengan DampingRatioNoBouncy sehingga tidak ada pantulan
            //                 dan parameter StiffnessMedium untuk membuat pegas sedikit kaku.
            ).background(color = color)
//            setel color sebagai pengubah latar belakang ke Column
        ){
//            Modifier.animateContentSize() untuk menganimasikan perubahan ukuran (tinggi item daftar).

            //    berisi Row yang menampilkan foto anjing dan informasi tentangnya.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                DogIcon(dog.imageResourceId)
//        DogIcon() menampilkan foto anjing.

                DogInformation(dog.name, dog.age)
//        DogInformation() berisi nama dan usia anjing.

//            Menambahkan pengatur jarak ke baris item daftar
//            menambahkan composable Spacer sebelum tombol luaskan dengan bobot 1f untuk
//            menyejajarkan ikon tombol dengan true.
                Spacer(modifier = modifier.weight(1f))

                DogItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
                /*
                         Operator NOT logis ( ! ) menampilkan nilai negatif dari ekspresi Boolean.
Misalnya, jika expanded adalah true, !expanded akan bernilai false.
                         */
            }

//            jika nilai expanded adalah true berarti informasi dogHoby tampilkan
            if(expanded){
                DogHobby(dog.hobbies, modifier = Modifier.padding(
                    start = dimensionResource(R.dimen.padding_medium),
                    top = dimensionResource(R.dimen.padding_small),
                    end = dimensionResource(R.dimen.padding_medium),
                    bottom = dimensionResource(R.dimen.padding_medium),
                ))
//            dog.hobbies diatas terhubung ke data/Dog.kt
            }

        }
    }

}


// menambahkan composable untuk menampilkan hobi
@Composable
fun DogHobby(@StringRes dogHoby: Int, modifier: Modifier = Modifier){
    Column(modifier = modifier){
        Text(text = stringResource(R.string.about), style = MaterialTheme.typography.labelSmall)
        Text(text = stringResource(dogHoby), style=MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun DogItemButton(
    expanded: Boolean,
    onClick: ()->Unit,
    modifier: Modifier = Modifier
){
    IconButton(onClick = onClick, modifier = modifier){
        Icon(
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary,
            imageVector = if(expanded){ Icons.Filled.ExpandLess}else{Icons.Filled.ExpandMore}

        )
    }
}

/**
 * Composable that displays a photo of a dog.
 *
 * @param dogIcon is the resource ID for the image of the dog
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogIcon(
    @DrawableRes dogIcon: Int,
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
//        modifier.clip() untuk memotong gambar menjadi sebuah bentuk

        contentScale = ContentScale.Crop,
// contentScale adalah atribut dari image, dan bukan bagian dari modifier.
//        ContentScale.Crop untuk membuat semua foto menjadi lingkaran

        painter = painterResource(dogIcon),

        // Content Description is not needed here - image is decorative, and setting a null content
        // description allows accessibility services to skip this element during navigation.

        contentDescription = null
    )
}

/**
 * Composable that displays a dog's name and age.
 *
 * @param dogName is the resource ID for the string of the dog's name
 * @param dogAge is the Int that represents the dog's age
 * @param modifier modifiers to set to this composable
 */
@Composable
fun DogInformation(
    @StringRes dogName: Int,
    dogAge: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(dogName),
            modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small)),
            style = MaterialTheme.typography.displayMedium
        )
//        displayMedium sebagai gaya untuk dogName

        Text(
            text = stringResource(R.string.years_old, dogAge),
            style = MaterialTheme.typography.bodyLarge
        )
//        bodyLarge sebagai gaya untuk dogAge karena berfungsi cukup baik dengan ukuran teks yang lebih kecil.
    }
}

/**
 * Composable that displays what the UI of the app looks like in light theme in the design tab.
 */
@Preview
@Composable
fun WoofPreview() {
//    WoofPreview() memungkinkan Anda melihat pratinjau aplikasi di panel Design.

    WoofTheme(darkTheme = false) {
        WoofApp()
    }
}
