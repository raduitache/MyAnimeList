package com.raduitache.myanimelist.auth.impl

import android.widget.Button
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.raduitache.myanimelist.BuildConfig
import com.raduitache.myanimelist.R
import com.raduitache.myanimelist.auth.AuthNavRoute
import com.raduitache.myanimelist.seasonal.impl.SeasonalViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject
import kotlin.math.min

class AuthNavRouteImpl @Inject constructor() : AuthNavRoute("auth/auth-screen", emptyList()) {

    @Composable
    override fun Content(onSignIn: () -> Unit) {
        val authViewModel: AuthViewModel = hiltViewModel()
        val state = authViewModel.authViewState.collectAsState().value

        var email by remember {
            mutableStateOf("")
        }

        var password by remember {
            mutableStateOf("")
        }

        var username by remember {
            mutableStateOf("")
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(width = 128.dp, height = 64.dp)
                        .background(Color.Red)
                )
                Box(modifier = Modifier.heightIn(min = 120.dp))
                TextField(value = email, label = {
                    Text(text = "Email")
                },
                    onValueChange = {
                        email = it

                    })
                Spacer(Modifier.size(4.dp))
                TextField(value = password,
                    label = {
                        Text(text = "Password")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        password = it

                    })
                if (state.isSigningUp) {
                    Spacer(Modifier.size(4.dp))
                    TextField(
                        value = username,
                        label = {
                            Text(text = "Username")
                        },
                        onValueChange = {
                            username = it
                        }
                    )
                }
                Spacer(Modifier.size(8.dp))
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else if (!state.shouldNavigateNext) {
                    ElevatedButton(onClick = {
                        if (state.isSigningUp) {
                            authViewModel.signUp(
                                email = email,
                                password = password,
                                username = username
                            )
                        } else {
                            authViewModel.login(
                                email = email,
                                password = password
                            )
                        }
                    }) {
                        Text(text = "Start")
                    }
                }


                state.hadError?.message?.let {
                    Spacer(Modifier.size(4.dp))
                    Text(text = it, style = TextStyle(color = MaterialTheme.colorScheme.error))
                }

                if (state.shouldNavigateNext) {
                    Spacer(Modifier.size(4.dp))
                    Text(
                        text = "Operation successful, you will be redirected shortly",
                        style = TextStyle(color = MaterialTheme.colorScheme.secondary)
                    )
                    LaunchedEffect(Unit) {
                        delay(1500)
                        onSignIn()
                    }
                }

                Box(modifier = Modifier.heightIn(min = 120.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AndroidView(
                        modifier = Modifier
                            .widthIn(max = 128.dp)
                            .heightIn(max = 128.dp),
                        factory = { context ->
                            Button(
                                context
                            ).apply {
                                text = context.getString(R.string.random_text)
                                setOnClickListener {
                                    println("Asd")
                                }
                                setTextColor(
                                    ContextCompat.getColorStateList(context,R.color.pressing_colors)
                                )
                            }
                        },
                    )
                    Button(onClick = { authViewModel.toggleSigningUp() }) {
                        Text("L2")
                    }
                    if (BuildConfig.DEBUG) {
                        AndroidView(
                            modifier = Modifier
                                .widthIn(max = 128.dp)
                                .heightIn(max = 128.dp), // Occupy the max size in the Compose UI tree
                            factory = { context ->
                                // Creates view
                                Button(context).apply {
                                    setOnClickListener {
                                        authViewModel.toggleUserList()
                                    }
                                    setBackgroundResource(R.drawable.selector)
                                }
                            },
                        )
                    }
                }
                Box(modifier = Modifier.heightIn(min = 120.dp))
            }

            state.users?.let {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Username", modifier = Modifier.weight(1f))
                        Text("Email", modifier = Modifier.weight(1f))
                        Text("Password", modifier = Modifier.weight(1f))
                    }
                }
                items(it) { user ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(user.username, modifier = Modifier.weight(1f))
                        Text(user.email, modifier = Modifier.weight(1f))
                        Text(
                            user.password,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1, modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}