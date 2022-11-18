package com.vision.andorid.composepaging

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.vision.andorid.composepaging.model.BeerDTO
import com.vision.andorid.composepaging.ui.theme.ComposePagingTheme
import com.vision.andorid.composepaging.viewmodel.BeerViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: BeerViewModel by viewModels()

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePagingTheme {
                Scaffold(topBar = {
                    TopAppBar(title = { Text(text = "Beer List") })
                },
                    content = {
                        Paging(viewModel)
                    })
            }
        }
    }
}


@Composable
fun Paging(viewModel: BeerViewModel) {

    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        pagingData.refresh()
        swipeRefreshState.isRefreshing = true
    }) {
        LazyColumn {

            if (pagingData.loadState.refresh is LoadState.Loading) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
            if (pagingData.loadState.refresh is LoadState.NotLoading) {
                swipeRefreshState.isRefreshing = false
                items(pagingData) {
                    it?.let {
                        ListItem(beer = it)
                    }
                }
            }
            if (pagingData.loadState.refresh is LoadState.Error) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        Text(text = "Error Occurred", modifier = Modifier.clickable {
                            pagingData.refresh()
                        })
                    }
                }
            }

            if (pagingData.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
            if (pagingData.loadState.append is LoadState.Error) {
                item {
                    ErrorFooter {
                        pagingData.retry()
                    }
                }
            }

            if (pagingData.loadState.prepend is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
            if (pagingData.loadState.prepend is LoadState.Error) {
                item {
                    ErrorHeader {
                        pagingData.retry()
                    }
                }
            }

        }
    }
}

@Composable
fun ErrorHeader(retry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            text = "Tap to Retry",
            modifier = Modifier
                .clickable { retry.invoke() }
                .align(Alignment.Center),
            style = MaterialTheme.typography.caption
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ErrorFooter(retry: () -> Unit = {}) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(imageVector = Icons.Default.Warning, contentDescription = null)
            Text(
                text = "Error Occurred",
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Retry",
                    modifier = Modifier
                        .clickable { retry.invoke() }
                        .align(Alignment.CenterEnd),
                    style = MaterialTheme.typography.caption
                )
            }
        }
    }

}


@Composable
fun ListItem(beer: BeerDTO) {

    Column(modifier = Modifier.padding(8.dp)) {

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            painter = rememberCoilPainter(
                request = ImageRequest.Builder(LocalContext.current).crossfade(true)
                    .data(beer.image_url).build()
            ), contentDescription = null
        )

        Text(
            text = "Name" + beer.name,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Desc: " + beer.description,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Contributed by: " + beer.contributed_by,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(8.dp)
        )

    }

}














