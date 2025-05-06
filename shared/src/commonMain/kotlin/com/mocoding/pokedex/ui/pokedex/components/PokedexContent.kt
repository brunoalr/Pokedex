package com.mocoding.pokedex.ui.pokedex.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mocoding.pokedex.ui.helper.LocalSafeArea
import com.mocoding.pokedex.ui.pokedex.PokedexComponent
import com.mocoding.pokedex.ui.pokedex.store.PokedexStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PokedexContent(
    state: PokedexStore.State,
    onEvent: (PokedexStore.Intent) -> Unit,
    onOutput: (PokedexComponent.Output) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onOutput(PokedexComponent.Output.NavigateBack)
                        },
                    ) {
                        Icon(Icons.Rounded.ArrowBackIosNew, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = Modifier.padding(LocalSafeArea.current)
    ) { paddingValue ->
        Box(
            modifier = Modifier.padding(paddingValue)
        ) {

//            TextField(
//                value = state.search,
//                onValueChange = { newSearch ->
//                    onEvent(MainStore.Intent.InputPokemonSearch(newSearch))
//                },
//                placeholder = {
//                    Text(text = "Search Pokemon")
//                },
//                leadingIcon = {
//                    IconButton(
//                        onClick = {}
//                    ) {
//                        Icon(Icons.Rounded.Search, contentDescription = "Search Pokemon")
//                    }
//                },
//                colors = TextFieldDefaults.textFieldColors(
//                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .2f),
//                    placeholderColor = MaterialTheme.colorScheme.surface,
//                    focusedLeadingIconColor = MaterialTheme.colorScheme.surface,
//                    unfocusedLeadingIconColor = MaterialTheme.colorScheme.surface,
//                    unfocusedIndicatorColor = Color.Transparent,
//                    focusedIndicatorColor = Color.Transparent,
//                ),
//                shape = MaterialTheme.shapes.extraLarge,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp, vertical = 20.dp)
//            )

            state.error?.let { error ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = error)
                }
            }

            Column {
                Text(
                    text = "Pokedex",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 6.dp)
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = .4f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                )

                if (state.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = .6f),
                        trackColor = MaterialTheme.colorScheme.outline.copy(alpha = .4f),
                    )
                }

                PokemonGrid(
                    onPokemonClicked = { name ->
                        onOutput(PokedexComponent.Output.NavigateToDetails(name = name))
                    },
                    pokemonList = state.pokemonList,
                    isLoading = !state.isLastPageLoaded,
                    loadMoreItems = {
                        if (state.pokemonList.isEmpty()) return@PokemonGrid

                        val nextPage = state.pokemonList.last().page + 1
                        onEvent(PokedexStore.Intent.LoadPokemonListByPage(page = nextPage))
                    }
                )
            }


        }
    }
}