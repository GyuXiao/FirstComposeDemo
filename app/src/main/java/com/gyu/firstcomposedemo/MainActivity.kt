package com.gyu.firstcomposedemo

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gyu.firstcomposedemo.ui.model.Message
import com.gyu.firstcomposedemo.ui.model.MsgData
import com.gyu.firstcomposedemo.ui.theme.FirstComposeDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirstComposeDemoTheme {
                Conversation(messages = MsgData.messages)
            }
        }
    }
}

/**
 * Row : x 坐标上的排列
 * Column : y 坐标上的排列
 * material design 围绕三点做设计————颜色、排版、形状
 */
@Composable
fun MessageCard(msg: Message) {

    // 创建一个能够检测卡片是否被展开的变量
    // remember 和 mutableStateOf
    var isExpanded by remember {
        mutableStateOf(false)
    }

    // 实现点击时 itemView 背景变色
    val surfaceColor by animateColorAsState(targetValue = if (isExpanded) Color(0xFFCCCCCC) else MaterialTheme.colors.surface)

    // 添加形状
    Surface(
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        modifier = Modifier
            .padding(all = 4.dp)
            .clickable {
                isExpanded = !isExpanded
            },
        color = surfaceColor
    ) {
        // 横向排版（一张图 和 一个纵向排版）
        Row(
            // 在 MessageCard 的四周增加 padding
            modifier = Modifier.padding(all = 4.dp)
        ) {
            Image(
                painterResource(id = R.drawable.ic_google),
                contentDescription = "profile picture",
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, MaterialTheme.colors.secondary, shape = CircleShape)
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            // 纵向排版 两个 Text
            Column {
                Text(
                    text = msg.author,
                    color = MaterialTheme.colors.secondaryVariant,
                    style = MaterialTheme.typography.subtitle2
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = msg.body,
                    // ！！！！！！
                    // 可能是系统提供的组件有 bug，那就是系统切换颜色模式后第一次点击 itemView 时，下面关于 textColor 的控制不可预期，而且是毕现
                    color = if (isSystemInDarkTheme()) Color.White else Color.Black,
                    style = MaterialTheme.typography.body2,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    // Composable 大小的动画效果
                    modifier = Modifier.animateContentSize()
                )
            }
        }
    }
}

/**
 * LazyColumn 有一个 items 子项。它接收一个 List 作为参数，
 * 它的 lambda 接收一个我们命名为 message 的参数
 * 而这个 lambda 将会调用每个 List 中里面提供的 item。
 */
@Composable
fun Conversation(messages: List<Message>) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(msg = message)
        }
    }
}

// 一个 @Preview 就是一个预览，奈斯
@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    FirstComposeDemoTheme {
        Conversation(messages = MsgData.messages)
    }
}
