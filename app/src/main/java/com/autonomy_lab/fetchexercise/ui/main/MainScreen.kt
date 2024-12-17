package com.autonomy_lab.fetchexercise.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.autonomy_lab.fetchexercise.R
import com.autonomy_lab.fetchexercise.data.network.FetchApiItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = hiltViewModel()
) {

    val list by viewModel.fetchItemList.collectAsState()
    val listIsEmpty by remember { derivedStateOf { list.isEmpty() } }
    val refreshing by viewModel.refreshing.collectAsState()

    val internetIsAvailable by viewModel.isInternetAvailable.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        Text(
                            text = stringResource(id = R.string.app_name),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        if (refreshing){
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.secondary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        }


                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ){
                            Button(
                                onClick = viewModel::getItemListFromNetwork
                            ) {
                                Text("Refresh")
                            }
                        }

                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color.Black),
            )
        },
        content = { innerPadding ->

            if (internetIsAvailable){
                if (listIsEmpty){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            text = "Nothing to show \n\n Please press Refresh for trying again",

                            )
                    }
                }else{

                    LazyColumn (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(vertical = 5.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        contentPadding = PaddingValues(top = 10.dp, bottom = 10.dp, )

                    ) {
                        items(list) { item ->

                            ItemContent(itemsByListId = item)

                        }
                    }
                }



            }else{
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow)
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        text = "No Internet Available \n\n Please connect to the Internet",

                    )
                }
            }



        }
    )

}


@Composable
fun ItemContent(
    modifier: Modifier = Modifier,
    itemsByListId: List<FetchApiItem>
) {



    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "List Id : ${itemsByListId[0].listId}"
        )

        Spacer(Modifier.width(8.dp))

        LazyRow (){
            items(itemsByListId.sortedBy { it.name }){ individualItem ->
                IndividualItem(
                    item = individualItem
                )
            }
        }

    }
    HorizontalDivider(
        modifier = Modifier
            .padding(top = 20.dp),
        thickness = 2.dp
    )

}


@Composable
fun IndividualItem(
    modifier: Modifier = Modifier,
    item: FetchApiItem
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
            .clip(shape = RoundedCornerShape(5.dp, 5.dp, 5.dp, 5.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center,
    ){
        Text(
            modifier = Modifier.padding(4.dp),
            text = "${item.name}"
        )
    }

}