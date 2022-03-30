package cn.my.jetpackcompose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.my.jetpackcompose.ui.theme.*

class MainActivity : ComponentActivity() {
    var currentLove: Love? by mutableStateOf(null)
    var currentLovePageState by mutableStateOf(LovePageState.Closed)
    var cardSize by mutableStateOf(IntSize(0, 0))
    var fullSize by mutableStateOf(IntSize(0, 0))
    var cardOffset by mutableStateOf(IntOffset(0, 0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
//            JetPackComposeTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Greeting("Android")
//                }
//            }
            Column(Modifier.onSizeChanged { fullSize = it }) {
                Column(
                    Modifier
                        .fillMaxWidth(1f)
                        .background(BackgroundWhite)
                        .verticalScroll(rememberScrollState())
                ) {
                    TopBar()
                    SearchBar()
                    NamesBar()
                    LovesArea(
                        { cardSize = it },
                        { love, offset ->
                            currentLove = love
                            currentLovePageState = LovePageState.Opening
                            cardOffset = offset
                        })

                    PlaceArea()
                }
                NavBar()
            }

        }
    }

    @Composable
    fun SearchBar() {
        Row(
            Modifier
                .padding(24.dp, 2.dp, 24.dp, 6.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var searchText by remember { mutableStateOf("") }
            BasicTextField(
                searchText, { searchText = it },
                Modifier
                    .padding(start = 24.dp)
                    .weight(1f),
                textStyle = TextStyle(fontSize = 15.sp)
            ) {
                if (searchText.isEmpty()) {
                    androidx.compose.material3.Text(
                        "搜搜看？",
                        color = Color(0xffb4b4b4),
                        fontSize = 15.sp
                    )
                }
                it()
            }
            Box(
                Modifier
                    .padding(6.dp)
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color(0xfffa9e51))
            ) {
                Icon(
                    painterResource(R.drawable.ic_search), "搜索",
                    Modifier
                        .size(24.dp)
                        .align(Alignment.Center), tint = Color.White
                )
            }
        }
    }

    @Composable
    fun NamesBar() {
        val names = listOf("扔物线", "朱凯", "老冯", "郝哥", "张三", "狐狸精", "孙悟空", "唐僧")
        var selected by remember { mutableStateOf(0) }
        LazyRow(Modifier.padding(0.dp, 8.dp), contentPadding = PaddingValues(12.dp, 0.dp)) {
            itemsIndexed(names) { index, name ->
                Column(
                    Modifier
                        .padding(12.dp, 4.dp)
                        .width(IntrinsicSize.Max)) {
                    androidx.compose.material3.Text(
                        name, fontSize = 15.sp,
                        color = if (index == selected) Color(0xfffa9e51) else Color(0xffb4b4b4)
                    )
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                            .height(2.dp)
                            .clip(RoundedCornerShape(1.dp))
                            .background(
                                if (index == selected) Color(0xfffa9e51) else Color.Transparent
                            )
                    )
                }
            }
        }
    }

    @Composable
    fun TopBar() {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(28.dp, 28.dp, 28.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(R.drawable.avatar_rengwuxian), "头像",
                Modifier
                    .clip(CircleShape)
                    .size(64.dp)
            )
            Column(
                Modifier
                    .padding(start = 14.dp)
                    .weight(1f)) {
                androidx.compose.material3.Text("欢迎回来！", fontSize = 14.sp, color = Gray)
                androidx.compose.material3.Text(
                    "小朱",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            androidx.compose.material3.Surface(Modifier.clip(CircleShape), color = LightPink) {
                Image(
                    painterResource(R.drawable.ic_notification_new), "通知",
                    Modifier
                        .padding(10.dp)
                        .size(32.dp)
                )
            }
        }
    }

    @Composable
    fun LovesArea(
        onCardSizedChanged: (IntSize) -> Unit,
        onCardClicked: (love: Love, offset: IntOffset) -> Unit
    ) {
        Column {
            Row(
                Modifier
                    .padding(24.dp, 12.dp)
                    .fillMaxWidth()
            ) {
                androidx.compose.material3.Text(
                    "TA 爱的",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                androidx.compose.material3.Text("查看全部", fontSize = 15.sp, color = Color(0xffb4b4b4))
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(24.dp, 0.dp)
            ) {
                itemsIndexed(loves) { index, love ->
                    var intOffset: IntOffset? by remember { mutableStateOf(null) }
                    androidx.compose.material3.Button(onClick = {
                        onCardClicked(
                            love,
                            intOffset!!
                        )
                    },
                        Modifier
                            .width(220.dp)
                            .onSizeChanged { if (index == 0) onCardSizedChanged(it) }
                            .onGloballyPositioned {
                                val offset = it.localToRoot(Offset(0f, 0f))
                                intOffset = IntOffset(offset.x.toInt(), offset.y.toInt())
                            },
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Column {
                            Image(
                                painterResource(love.imageId), "图像",
                                Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .fillMaxWidth()
                                    .aspectRatio(1.35f),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )
                            Row(
                                Modifier.padding(8.dp, 12.dp, 8.dp, 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column() {
                                    androidx.compose.material3.Text(
                                        love.name,
                                        color = Color.Black,
                                        fontSize = 15.sp
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    androidx.compose.material3.Text(
                                        love.category,
                                        color = Color(0xffb4b4b4),
                                        fontSize = 14.sp
                                    )
                                }
                                Spacer(Modifier.weight(1f))
                                Row(
                                    Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color(0xfffef1e6))
                                        .padding(6.dp, 11.dp, 8.dp, 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painterResource(R.drawable.ic_star),
                                        "",
                                        Modifier.size(24.dp),
                                        tint = Color(0xfffa9e51)
                                    )
                                    androidx.compose.material3.Text(
                                        love.score.toString(),
                                        color = Color(0xfffa9e51),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PlaceArea() {
        val place = Place("扔物线学堂办公室", "郑州", "5 分钟前", R.drawable.img_xuetang)
        Column(Modifier.padding(24.dp, 24.dp, 24.dp, 0.dp)) {
            androidx.compose.material3.Text(
                "TA 去过", fontSize = 16.sp, fontWeight = FontWeight.Bold
            )
            androidx.compose.material3.Surface(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(8.dp),
            ) {
                Row(Modifier.height(IntrinsicSize.Max)) {
                    Image(
                        painterResource(place.imageId),
                        "图像",
                        Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .size(80.dp),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                    Column(
                        Modifier
                            .padding(12.dp, 0.dp)
                            .fillMaxHeight(), verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        androidx.compose.material3.Text(
                            place.time,
                            fontSize = 14.sp,
                            color = Color(0xffb4b4b4)
                        )
                        androidx.compose.material3.Text(place.name, fontSize = 16.sp)
                        androidx.compose.material3.Text(
                            place.city,
                            fontSize = 14.sp,
                            color = Color(0xffb4b4b4)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun NavBar() {
        Row(
            Modifier
                .background(Color.White)
                .height(84.dp)
                .padding(16.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(R.drawable.ic_home, "首页", Orange)
            NavItem(R.drawable.ic_tag, "标签", Gray)
            NavItem(R.drawable.ic_calendar, "日历", Gray)
            NavItem(R.drawable.ic_me, "我的", Gray)
        }
    }

    @Composable
    fun RowScope.NavItem(@DrawableRes iconRes: Int, description: String, tint: Color) {
        androidx.compose.material3.Button(
            onClick = {
                Toast.makeText(this@MainActivity, description, Toast.LENGTH_LONG).show()
            },
            Modifier
                .weight(1f)
                .fillMaxHeight(),
            shape = RectangleShape,
            colors = ButtonDefaults.outlinedButtonColors()
        ) {
            Icon(
                painterResource(iconRes), description,
                Modifier
                    .size(24.dp)
                    .weight(1f),
                tint = tint
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetPackComposeTheme {
        Greeting("Android")
    }
}


data class Place(
    val name: String,
    val city: String,
    val time: String,
    @DrawableRes val imageId: Int
)


data class Love(
    val name: String,
    val category: String,
    val score: Float,
    val scoreText: String,
    @DrawableRes val imageId: Int,
    val description: String
)


val loves = mutableStateListOf(
    Love("静电容键盘", "生产工具", 4.4f, "比较喜欢", R.drawable.img_keyboard, "好东西"),
    Love("烤串", "心情修复用品", 4.8f, "非常喜欢", R.drawable.img_kaochuan, "好东西"),
    Love(
        "老婆",
        "赚钱为了啥？",
        5f,
        "完美无瑕",
        R.drawable.img_laopo,
        """
      忘了是怎么开始 也许就是对你 有一种感觉
      忽然间发现自己 已深深爱上你 真的很简单

      爱的地暗天黑都已无所谓 是是非非无法抉择
      没有后悔为爱日夜去跟随 那个疯狂的人是我

      I love you 无法不爱你 baby 说你也爱我
      I love you 永远不愿意 baby 失去你

      不可能更快乐 只要能在一起 做什么都可以
      虽然 世界变个不停 用最真诚的心 让爱变得简单
      
      爱的地暗天黑都已无所谓 是是非非无法抉择
      没有后悔为爱日夜去跟随 那个疯狂的人是我

      I love you 一直在这里 baby 一直在爱你
      I love you 永远都不放弃 这爱你的权利
      如果你还有一些困惑 贴着我的心倾听
      听我说着爱你 Yes I do

      I love you 永远都不放弃 这爱你的权力
    """.trimIndent()
    )
)

enum class LovePageState {
    Closing, Closed, Opening, Open
}