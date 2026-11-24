package com.example.firebase_prac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.firebase_prac.ui.EDIT_TASK_SCREEN
import com.example.firebase_prac.ui.SETTINGS_SCREEN
import com.example.firebase_prac.ui.SIGN_ON_SCREEN
import com.example.firebase_prac.ui.SIGN_UP_SCREEN
import com.example.firebase_prac.ui.SPLASH_SCREEN
import com.example.firebase_prac.ui.TASK_ID
import com.example.firebase_prac.ui.TASK_ID_ARG
import com.example.firebase_prac.ui.TASKS_SCREEN
import com.example.firebase_prac.ui.edittask.EditTaskScreen
import com.example.firebase_prac.ui.settings.SettingsScreen
import com.example.firebase_prac.ui.sign.signon.SignOnScreen
import com.example.firebase_prac.ui.sign.signup.SignUpScreen
import com.example.firebase_prac.ui.splash.SplashScreen
import com.example.firebase_prac.ui.tasks.TasksScreen
import com.example.firebase_prac.ui.theme.Firebase_pracTheme
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Firebase_pracTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MakeItSoApp()
                }
            }
        }
    }
}

@Composable
fun MakeItSoApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SPLASH_SCREEN
    ) {
        composable(SPLASH_SCREEN) {
            SplashScreen(
                openAndPopUp = { route, popUp ->
                    navController.navigate(route) {
                        popUpTo(popUp) { inclusive = true }
                    }
                }
            )
        }

        composable(SIGN_ON_SCREEN) {
            SignOnScreen(
                openAndPopUp = { route, popUp ->
                    navController.navigate(route) {
                        popUpTo(popUp) { inclusive = true }
                    }
                },
                openScreen = { route -> navController.navigate(route) }
            )
        }

        composable(SIGN_UP_SCREEN) {
            SignUpScreen(
                openAndPopUp = { route, popUp ->
                    navController.navigate(route) {
                        popUpTo(popUp) { inclusive = true }
                    }
                }
            )
        }

        composable(TASKS_SCREEN) {
            TasksScreen(
                openScreen = { route -> navController.navigate(route) }
            )
        }

        composable(
            route = "$EDIT_TASK_SCREEN$TASK_ID_ARG",
            arguments = listOf(navArgument(TASK_ID) {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString(TASK_ID) ?: ""
            val viewModel: com.example.firebase_prac.ui.edittask.EditTaskViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            androidx.compose.runtime.LaunchedEffect(taskId) {
                viewModel.initialize(taskId)
            }
            EditTaskScreen(
                popUpScreen = { navController.popBackStack() },
                viewModel = viewModel
            )
        }

        composable(SETTINGS_SCREEN) {
            SettingsScreen(
                popUpScreen = { navController.popBackStack() },
                openScreen = { route -> navController.navigate(route) },
                openAndPopUp = { route, popUp ->
                    navController.navigate(route) {
                        popUpTo(popUp) { inclusive = true }
                    }
                }
            )
        }
    }
}
