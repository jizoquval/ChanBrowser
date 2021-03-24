package com.jizoquval.chanBrowser.androidApp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.jizoquval.chanBrowser.androidApp.ui.screen.BoardScreen
import com.jizoquval.chanBrowser.androidApp.ui.screen.BoardsListScreen
import com.jizoquval.chanBrowser.androidApp.ui.screen.SettingsScreen
import com.jizoquval.chanBrowser.androidApp.ui.screen.ThreadScreen

object Routes {
    const val boardsList = "boardsList"

    const val board = "board"
    const val boardId = "boardId"

    const val thread = "thread"
    const val threadId = "threadId"

    const val settings = "settings"
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { NavigationActions(navController) }

    NavHost(navController, startDestination = Routes.boardsList) {
        composable(Routes.boardsList) {
            BoardsListScreen(actions.toBoard, actions.toSettings)
        }
        composable(Routes.settings) {
            SettingsScreen(actions.upPress)
        }
        composable(
            "${Routes.board}/{${Routes.boardId}}",
            arguments = listOf(navArgument(Routes.boardId) { type = NavType.StringType })
        ) { backStackEntry ->
            val boardId = checkNotNull(backStackEntry.arguments?.getString(Routes.boardId)) {
                "board id not passed to board screen"
            }
            BoardScreen(boardId = boardId, toThread = actions.toThread, pressUp = actions.upPress)
        }
        composable(
            "${Routes.thread}/{${Routes.threadId}}",
            arguments = listOf(navArgument(Routes.threadId) { type = NavType.StringType })
        ) { backStackEntry ->
            val threadId = checkNotNull(backStackEntry.arguments?.getString(Routes.threadId)) {
                "thread id not passed to board screen"
            }
            ThreadScreen(threadId = threadId, pressUp = actions.upPress)
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(navController: NavHostController) {

    val toBoard: (String) -> Unit = { boardId ->
        navController.navigate("${Routes.board}/$boardId")
    }
    val toThread: (String) -> Unit = { threadId ->
        navController.navigate("${Routes.thread}/$threadId")
    }
    val toSettings: () -> Unit = {
        navController.navigate(Routes.settings)
    }
    val upPress: () -> Unit = {
        navController.navigateUp()
    }
}