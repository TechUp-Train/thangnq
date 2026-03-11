package com.apero.composetraining.session5.exercises.auth_exercise.main_flow

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.apero.composetraining.session5.exercises.auth_exercise.*
import com.apero.composetraining.session5.exercises.auth_exercise.data.Tab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainFlow(
    onLogout: () -> Unit
) {
    val feedStack = rememberNavBackStack(FeedKey)
    val discoverStack = rememberNavBackStack(DiscoverKey)
    val profileStack = rememberNavBackStack(ProfileKey)

    var currentTab by rememberSaveable { mutableStateOf(Tab.FEED) }

    val currentStack = when (currentTab) {
        Tab.FEED -> feedStack
        Tab.DISCOVER -> discoverStack
        Tab.PROFILE -> profileStack
    }

    val currentKey = currentStack.lastOrNull()
    val showBackButton = currentStack.size > 1

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (currentKey) {
                            is PostDetailKey -> "Post Detail"
                            is EditProfileKey -> "Edit Profile"
                            is FeedKey -> "Feed"
                            is DiscoverKey -> "Discover"
                            is ProfileKey -> "Profile"
                            else -> ""
                        }
                    )
                },
                navigationIcon = {
                    if (showBackButton) {
                        IconButton(
                            onClick = {
                                if (currentStack.size > 1) {
                                    currentStack.removeLastOrNull()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Feed") },
                    selected = currentTab == Tab.FEED,
                    onClick = { currentTab = Tab.FEED }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Explore, contentDescription = null) },
                    label = { Text("Discover") },
                    selected = currentTab == Tab.DISCOVER,
                    onClick = { currentTab = Tab.DISCOVER }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Profile") },
                    selected = currentTab == Tab.PROFILE,
                    onClick = { currentTab = Tab.PROFILE }
                )
            }
        }
    ) { paddingValues ->
        NavDisplay(
            backStack = currentStack,
            onBack = {
                if (currentStack.size > 1) {
                    currentStack.removeLastOrNull()
                }
            },
            entryProvider = entryProvider {
                entry<FeedKey> {
                    FeedScreen(
                        onPostClick = { post ->
                            feedStack.add(PostDetailKey(post))
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                entry<PostDetailKey> { key ->
                    PostDetailScreen(
                        post = key.post,
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                entry<DiscoverKey> {
                    DiscoverScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                entry<ProfileKey> {
                    ProfileScreen(
                        onEditProfile = {
                            profileStack.add(EditProfileKey)
                        },
                        onLogout = onLogout,
                        modifier = Modifier.padding(paddingValues)
                    )
                }

                entry<EditProfileKey> {
                    EditProfileScreen(
                        onBack = {
                            if (profileStack.size > 1) {
                                profileStack.removeLastOrNull()
                            }
                        },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun MainFlowPreview() {
    MainFlow(onLogout = {})
}