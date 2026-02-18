package com.example.taskdego.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.taskdego.data.dao.*
import com.example.taskdego.data.entity.*
import com.example.taskdego.data.seed.PokemonSeedData  // ★ 追加
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        tTaskEntity::class,
        tTrainerProfileEntity::class,
        mItemEntity::class,
        tItemEntity::class,
        mLocationEntity::class,
        mLocationSpawnEntity::class,
        mPokemonEntity::class  // ★ 追加
    ],
    version = 3, // ★ バージョンを上げる
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tTaskDao(): tTaskDao
    abstract fun tTrainerProfileDao(): tTrainerProfileDao
    abstract fun mItemDao(): mItemDao
    abstract fun tItemDao(): tItemDao
    abstract fun mLocationDao(): mLocationDao
    abstract fun mLocationSpawnDao(): mLocationSpawnDao

    abstract fun mPokemonDao(): mPokemonDao  // ★ 追加

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                //開発中のみ、全部削除
//                context.deleteDatabase("task_dego_database")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_dego_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateDatabase(database)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateDatabase(database: AppDatabase) {
            val mItemDao = database.mItemDao()
            val tTrainerProfileDao = database.tTrainerProfileDao()
            val mLocationDao = database.mLocationDao()
            val mLocationSpawnDao = database.mLocationSpawnDao()
            val mPokemonDao = database.mPokemonDao()  // ★ 追加

            // ==========================================
            // アイテムマスターデータ（全73種類）
            // ==========================================
            listOf(
                mItemEntity(1, "モンスターボール", 500, false),
                mItemEntity(2, "スーパーボール", 1500, false),
                mItemEntity(3, "ハイパーボール", 3000, false),
                mItemEntity(4, "ズリのみ", null, false),
                mItemEntity(5, "パイルのみ", null, false),
                mItemEntity(6, "ふしぎなアメ", null, false),
                mItemEntity(7, "ツタージャのアメ", null, true),
                mItemEntity(8, "ポカブのアメ", null, true),
                mItemEntity(9, "ミジュマルのアメ", null, true),
                mItemEntity(10, "チラーミィのアメ", null, true),
                mItemEntity(11, "マメパトのアメ", null, true),
                mItemEntity(12, "コフキムシのアメ", null, true),
                mItemEntity(13, "クルミルのアメ", null, true),
                mItemEntity(14, "フシデのアメ", null, true),
                mItemEntity(15, "ピチューのアメ", null, true),
                mItemEntity(16, "ルリリのアメ", null, true),
                mItemEntity(17, "トゲピーのアメ", null, true),
                mItemEntity(18, "パモのアメ", null, true),
                mItemEntity(19, "シキジカのアメ", null, true),
                mItemEntity(20, "ヒメンカのアメ", null, true),
                mItemEntity(21, "フラベベのアメ", null, true),
                mItemEntity(22, "ニャオハのアメ", null, true),
                mItemEntity(23, "ケロマツのアメ", null, true),
                mItemEntity(24, "ズピカのアメ", null, true),
                mItemEntity(25, "シママのアメ", null, true),
                mItemEntity(26, "モグリューのアメ", null, true),
                mItemEntity(27, "ユニランのアメ", null, true),
                mItemEntity(28, "ニャスパーのアメ", null, true),
                mItemEntity(29, "キノココのアメ", null, true),
                mItemEntity(30, "モンメンのアメ", null, true),
                mItemEntity(31, "チュリネのアメ", null, true),
                mItemEntity(32, "パチリスのアメ", null, true),
                mItemEntity(33, "エモンガのアメ", null, true),
                mItemEntity(34, "ヘラクロスのアメ", null, true),
                mItemEntity(35, "イーブイのアメ", null, true),
                mItemEntity(36, "カルボウのアメ", null, true),
                mItemEntity(37, "カイデンのアメ", null, true),
                mItemEntity(38, "アシマリのアメ", null, true),
                mItemEntity(39, "ケイコウオのアメ", null, true),
                mItemEntity(40, "ウデッポウのアメ", null, true),
                mItemEntity(41, "シビシラスのアメ", null, true),
                mItemEntity(42, "ミガルーサのアメ", null, true),
                mItemEntity(43, "ダダリンのアメ", null, true),
                mItemEntity(44, "ヤヤコマのアメ", null, true),
                mItemEntity(45, "コジョフーのアメ", null, true),
                mItemEntity(46, "カヌチャンのアメ", null, true),
                mItemEntity(47, "グライガーのアメ", null, true),
                mItemEntity(48, "チゴラスのアメ", null, true),
                mItemEntity(49, "ヒトツキのアメ", null, true),
                mItemEntity(50, "ムックルのアメ", null, true),
                mItemEntity(51, "ムクバードのアメ", null, true),
                mItemEntity(52, "ミブリムのアメ", null, true),
                mItemEntity(53, "ゾロアのアメ", null, true),
                mItemEntity(54, "ボクレーのアメ", null, true),
                mItemEntity(55, "ヒトモシのアメ", null, true),
                mItemEntity(56, "クレッフィのアメ", null, true),
                mItemEntity(57, "ヌメラのアメ", null, true),
                mItemEntity(58, "ミニリュウのアメ", null, true),
                mItemEntity(59, "ロコンのアメ", null, true),
                mItemEntity(60, "アマルスのアメ", null, true),
                mItemEntity(61, "リオルのアメ", null, true),
                mItemEntity(62, "アイアントのアメ", null, true),
                mItemEntity(63, "モノズのアメ", null, true),
                mItemEntity(64, "メラルバのアメ", null, true),
                mItemEntity(65, "マッシブーンのアメ", null, true),
                mItemEntity(66, "オーガポンのアメ", null, true),
                mItemEntity(67, "ブリザポスのアメ", null, true),
                mItemEntity(68, "レイスポスのアメ", null, true),
                mItemEntity(69, "バドレックスのアメ", null, true),
                mItemEntity(70, "カイオーガのアメ", null, true),
                mItemEntity(71, "レシラムのアメ", null, true),
                mItemEntity(72, "メロエッタのアメ", null, true),
                mItemEntity(73, "アルセウスのアメ", null, true)
            ).forEach { mItemDao.insertItem(it) }

            // ==========================================
            // トレーナー初期データ
            // ==========================================
            tTrainerProfileDao.insertTrainer(
                tTrainerProfileEntity(
                    t_trainer_id = 1,
                    trainer_name = "たろう",
                    level = 1,
                    exp = 0,
                    coins = 0,
                    daily_reword_count_task = 0,
                    daily_reword_count_routine = 0
                )
            )

            // ==========================================
            // エリアマスターデータ（全27エリア）
            // ==========================================
            listOf(
                mLocationEntity(1, "1番エリア", 1),
                mLocationEntity(2, "2番エリア", 2),
                mLocationEntity(3, "アンプリールの森", 3),
                mLocationEntity(4, "3番エリア", 4),
                mLocationEntity(5, "カランドルの花園", 5),
                mLocationEntity(6, "4番エリア", 6),
                mLocationEntity(7, "こだまの横穴", 7),
                mLocationEntity(8, "5番エリア", 8),
                mLocationEntity(9, "6番エリア", 9),
                mLocationEntity(10, "7番エリア", 10),
                mLocationEntity(11, "8番エリア", 11),
                mLocationEntity(12, "ヴェルデ樹林", 12),
                mLocationEntity(13, "9番エリア", 13),
                mLocationEntity(14, "セントラル・ゲートウェイ", 14),
                mLocationEntity(15, "10番エリア", 15),
                mLocationEntity(16, "アジュール湾", 16),
                mLocationEntity(17, "ソレイユ島", 17),
                mLocationEntity(18, "11番エリア", 18),
                mLocationEntity(19, "クロワ大山脈", 19),
                mLocationEntity(20, "イピー遺跡", 20),
                mLocationEntity(21, "12番エリア", 21),
                mLocationEntity(22, "トワイライト樹海", 22),
                mLocationEntity(23, "ロスト・レジデンス", 23),
                mLocationEntity(24, "龍神の湖", 24),
                mLocationEntity(25, "13番エリア", 25),
                mLocationEntity(26, "チャンピオンロード", 26),
                mLocationEntity(27, "クロワ・ボルケーノ", 27)
            ).forEach { mLocationDao.insertLocation(it) }

            // ==========================================
            // 出現ポケモンデータ（全エリア）
            // ==========================================
            listOf(
                // エリア1
                mLocationSpawnEntity(location_id=1, pokemon_id=100, pokemon_name="チラーミィ", spawn_rate=30),
                mLocationSpawnEntity(location_id=1, pokemon_id=120, pokemon_name="マメパト", spawn_rate=40),
                mLocationSpawnEntity(location_id=1, pokemon_id=150, pokemon_name="コフキムシ", spawn_rate=30),
                // エリア2
                mLocationSpawnEntity(location_id=2, pokemon_id=100, pokemon_name="チラーミィ", spawn_rate=28),
                mLocationSpawnEntity(location_id=2, pokemon_id=120, pokemon_name="マメパト", spawn_rate=39),
                mLocationSpawnEntity(location_id=2, pokemon_id=150, pokemon_name="コフキムシ", spawn_rate=28),
                mLocationSpawnEntity(location_id=2, pokemon_id=240, pokemon_name="ピチュー", spawn_rate=5),
                // エリア3
                mLocationSpawnEntity(location_id=3, pokemon_id=100, pokemon_name="チラーミィ", spawn_rate=10),
                mLocationSpawnEntity(location_id=3, pokemon_id=120, pokemon_name="マメパト", spawn_rate=18),
                mLocationSpawnEntity(location_id=3, pokemon_id=150, pokemon_name="コフキムシ", spawn_rate=24),
                mLocationSpawnEntity(location_id=3, pokemon_id=180, pokemon_name="クルミル", spawn_rate=24),
                mLocationSpawnEntity(location_id=3, pokemon_id=210, pokemon_name="フシデ", spawn_rate=18),
                mLocationSpawnEntity(location_id=3, pokemon_id=240, pokemon_name="ピチュー", spawn_rate=5),
                mLocationSpawnEntity(location_id=3, pokemon_id=250, pokemon_name="ピカチュウ", spawn_rate=1),
                // エリア4
                mLocationSpawnEntity(location_id=4, pokemon_id=100, pokemon_name="チラーミィ", spawn_rate=15),
                mLocationSpawnEntity(location_id=4, pokemon_id=360, pokemon_name="シキジカ春", spawn_rate=15),
                mLocationSpawnEntity(location_id=4, pokemon_id=120, pokemon_name="マメパト", spawn_rate=22),
                mLocationSpawnEntity(location_id=4, pokemon_id=180, pokemon_name="クルミル", spawn_rate=22),
                mLocationSpawnEntity(location_id=4, pokemon_id=330, pokemon_name="パモ", spawn_rate=19),
                mLocationSpawnEntity(location_id=4, pokemon_id=270, pokemon_name="ルリリ", spawn_rate=1),
                mLocationSpawnEntity(location_id=4, pokemon_id=280, pokemon_name="マリル", spawn_rate=4),
                // エリア5
                mLocationSpawnEntity(location_id=5, pokemon_id=380, pokemon_name="ヒメンカ", spawn_rate=20),
                mLocationSpawnEntity(location_id=5, pokemon_id=400, pokemon_name="フラベベ", spawn_rate=25),
                mLocationSpawnEntity(location_id=5, pokemon_id=150, pokemon_name="コフキムシ", spawn_rate=21),
                mLocationSpawnEntity(location_id=5, pokemon_id=160, pokemon_name="コフーライ", spawn_rate=3),
                mLocationSpawnEntity(location_id=5, pokemon_id=170, pokemon_name="ビビヨン", spawn_rate=1),
                mLocationSpawnEntity(location_id=5, pokemon_id=360, pokemon_name="シキジカ春", spawn_rate=10),
                mLocationSpawnEntity(location_id=5, pokemon_id=180, pokemon_name="クルミル", spawn_rate=20),
                // エリア6
                mLocationSpawnEntity(location_id=6, pokemon_id=360, pokemon_name="シキジカ春", spawn_rate=20),
                mLocationSpawnEntity(location_id=6, pokemon_id=120, pokemon_name="マメパト", spawn_rate=20),
                mLocationSpawnEntity(location_id=6, pokemon_id=170, pokemon_name="ビビヨン", spawn_rate=5),
                mLocationSpawnEntity(location_id=6, pokemon_id=270, pokemon_name="ルリリ", spawn_rate=2),
                mLocationSpawnEntity(location_id=6, pokemon_id=280, pokemon_name="マリル", spawn_rate=5),
                mLocationSpawnEntity(location_id=6, pokemon_id=380, pokemon_name="ヒメンカ", spawn_rate=19),
                mLocationSpawnEntity(location_id=6, pokemon_id=490, pokemon_name="ズピカ", spawn_rate=19),
                mLocationSpawnEntity(location_id=6, pokemon_id=430, pokemon_name="ニャオハ", spawn_rate=5),
                mLocationSpawnEntity(location_id=6, pokemon_id=460, pokemon_name="ケロマツ", spawn_rate=5),
                // エリア7
                mLocationSpawnEntity(location_id=7, pokemon_id=530, pokemon_name="モグリュー", spawn_rate=50),
                mLocationSpawnEntity(location_id=7, pokemon_id=210, pokemon_name="フシデ", spawn_rate=30),
                mLocationSpawnEntity(location_id=7, pokemon_id=550, pokemon_name="ユニラン", spawn_rate=20),
                // エリア8
                mLocationSpawnEntity(location_id=8, pokemon_id=120, pokemon_name="マメパト", spawn_rate=20),
                mLocationSpawnEntity(location_id=8, pokemon_id=180, pokemon_name="クルミル", spawn_rate=20),
                mLocationSpawnEntity(location_id=8, pokemon_id=210, pokemon_name="フシデ", spawn_rate=20),
                mLocationSpawnEntity(location_id=8, pokemon_id=510, pokemon_name="シママ", spawn_rate=15),
                mLocationSpawnEntity(location_id=8, pokemon_id=550, pokemon_name="ユニラン", spawn_rate=15),
                mLocationSpawnEntity(location_id=8, pokemon_id=580, pokemon_name="ニャスパー", spawn_rate=5),
                mLocationSpawnEntity(location_id=8, pokemon_id=430, pokemon_name="ニャオハ", spawn_rate=5),
                // エリア9
                mLocationSpawnEntity(location_id=9, pokemon_id=120, pokemon_name="マメパト", spawn_rate=23),
                mLocationSpawnEntity(location_id=9, pokemon_id=180, pokemon_name="クルミル", spawn_rate=24),
                mLocationSpawnEntity(location_id=9, pokemon_id=210, pokemon_name="フシデ", spawn_rate=23),
                mLocationSpawnEntity(location_id=9, pokemon_id=510, pokemon_name="シママ", spawn_rate=10),
                mLocationSpawnEntity(location_id=9, pokemon_id=580, pokemon_name="ニャスパー", spawn_rate=5),
                mLocationSpawnEntity(location_id=9, pokemon_id=600, pokemon_name="キノココ", spawn_rate=10),
                mLocationSpawnEntity(location_id=9, pokemon_id=430, pokemon_name="ニャオハ", spawn_rate=5),
                // エリア10
                mLocationSpawnEntity(location_id=10, pokemon_id=100, pokemon_name="チラーミィ", spawn_rate=18),
                mLocationSpawnEntity(location_id=10, pokemon_id=180, pokemon_name="クルミル", spawn_rate=18),
                mLocationSpawnEntity(location_id=10, pokemon_id=380, pokemon_name="ヒメンカ", spawn_rate=17),
                mLocationSpawnEntity(location_id=10, pokemon_id=510, pokemon_name="シママ", spawn_rate=15),
                mLocationSpawnEntity(location_id=10, pokemon_id=620, pokemon_name="モンメン", spawn_rate=16),
                mLocationSpawnEntity(location_id=10, pokemon_id=640, pokemon_name="チュリネ", spawn_rate=16),
                // エリア11
                mLocationSpawnEntity(location_id=11, pokemon_id=100, pokemon_name="チラーミィ", spawn_rate=20),
                mLocationSpawnEntity(location_id=11, pokemon_id=120, pokemon_name="マメパト", spawn_rate=20),
                mLocationSpawnEntity(location_id=11, pokemon_id=180, pokemon_name="クルミル", spawn_rate=20),
                mLocationSpawnEntity(location_id=11, pokemon_id=330, pokemon_name="パモ", spawn_rate=20),
                mLocationSpawnEntity(location_id=11, pokemon_id=510, pokemon_name="シママ", spawn_rate=15),
                mLocationSpawnEntity(location_id=11, pokemon_id=430, pokemon_name="ニャオハ", spawn_rate=5),
                // エリア12
                mLocationSpawnEntity(location_id=12, pokemon_id=120, pokemon_name="マメパト", spawn_rate=12),
                mLocationSpawnEntity(location_id=12, pokemon_id=130, pokemon_name="ハトーボー", spawn_rate=3),
                mLocationSpawnEntity(location_id=12, pokemon_id=180, pokemon_name="クルミル", spawn_rate=15),
                mLocationSpawnEntity(location_id=12, pokemon_id=190, pokemon_name="クルマユ", spawn_rate=5),
                mLocationSpawnEntity(location_id=12, pokemon_id=210, pokemon_name="フシデ", spawn_rate=12),
                mLocationSpawnEntity(location_id=12, pokemon_id=220, pokemon_name="ホイーガ", spawn_rate=3),
                mLocationSpawnEntity(location_id=12, pokemon_id=600, pokemon_name="キノココ", spawn_rate=15),
                mLocationSpawnEntity(location_id=12, pokemon_id=660, pokemon_name="パチリス", spawn_rate=10),
                mLocationSpawnEntity(location_id=12, pokemon_id=670, pokemon_name="エモンガ", spawn_rate=10),
                mLocationSpawnEntity(location_id=12, pokemon_id=580, pokemon_name="ニャスパー", spawn_rate=5),
                mLocationSpawnEntity(location_id=12, pokemon_id=680, pokemon_name="ヘラクロス", spawn_rate=10),
                // エリア13
                mLocationSpawnEntity(location_id=13, pokemon_id=330, pokemon_name="パモ", spawn_rate=25),
                mLocationSpawnEntity(location_id=13, pokemon_id=340, pokemon_name="パモット", spawn_rate=5),
                mLocationSpawnEntity(location_id=13, pokemon_id=100, pokemon_name="チラーミィ", spawn_rate=30),
                mLocationSpawnEntity(location_id=13, pokemon_id=510, pokemon_name="シママ", spawn_rate=30),
                mLocationSpawnEntity(location_id=13, pokemon_id=430, pokemon_name="ニャオハ", spawn_rate=5),
                mLocationSpawnEntity(location_id=13, pokemon_id=690, pokemon_name="イーブイ", spawn_rate=5),
                // エリア14
                mLocationSpawnEntity(location_id=14, pokemon_id=530, pokemon_name="モグリュー", spawn_rate=40),
                mLocationSpawnEntity(location_id=14, pokemon_id=550, pokemon_name="ユニラン", spawn_rate=25),
                mLocationSpawnEntity(location_id=14, pokemon_id=210, pokemon_name="フシデ", spawn_rate=25),
                mLocationSpawnEntity(location_id=14, pokemon_id=780, pokemon_name="カルボウ", spawn_rate=10),
                // エリア15
                mLocationSpawnEntity(location_id=15, pokemon_id=330, pokemon_name="パモ", spawn_rate=25),
                mLocationSpawnEntity(location_id=15, pokemon_id=340, pokemon_name="パモット", spawn_rate=5),
                mLocationSpawnEntity(location_id=15, pokemon_id=100, pokemon_name="チラーミィ", spawn_rate=25),
                mLocationSpawnEntity(location_id=15, pokemon_id=510, pokemon_name="シママ", spawn_rate=30),
                mLocationSpawnEntity(location_id=15, pokemon_id=780, pokemon_name="カルボウ", spawn_rate=5),
                mLocationSpawnEntity(location_id=15, pokemon_id=430, pokemon_name="ニャオハ", spawn_rate=5),
                mLocationSpawnEntity(location_id=15, pokemon_id=690, pokemon_name="イーブイ", spawn_rate=5),
                // エリア16
                mLocationSpawnEntity(location_id=16, pokemon_id=810, pokemon_name="カイデン", spawn_rate=23),
                mLocationSpawnEntity(location_id=16, pokemon_id=830, pokemon_name="アシマリ", spawn_rate=5),
                mLocationSpawnEntity(location_id=16, pokemon_id=860, pokemon_name="ケイコウオ", spawn_rate=23),
                mLocationSpawnEntity(location_id=16, pokemon_id=880, pokemon_name="ウデッポウ", spawn_rate=22),
                mLocationSpawnEntity(location_id=16, pokemon_id=900, pokemon_name="シビシラス", spawn_rate=10),
                mLocationSpawnEntity(location_id=16, pokemon_id=930, pokemon_name="ミガルーサ", spawn_rate=15),
                mLocationSpawnEntity(location_id=16, pokemon_id=940, pokemon_name="ダダリン", spawn_rate=2),
                // エリア17
                mLocationSpawnEntity(location_id=17, pokemon_id=810, pokemon_name="カイデン", spawn_rate=25),
                mLocationSpawnEntity(location_id=17, pokemon_id=830, pokemon_name="アシマリ", spawn_rate=5),
                mLocationSpawnEntity(location_id=17, pokemon_id=361, pokemon_name="シキジカ夏", spawn_rate=30),
                mLocationSpawnEntity(location_id=17, pokemon_id=640, pokemon_name="チュリネ", spawn_rate=30),
                mLocationSpawnEntity(location_id=17, pokemon_id=650, pokemon_name="ドレディア", spawn_rate=5),
                mLocationSpawnEntity(location_id=17, pokemon_id=261, pokemon_name="Aライチュウ", spawn_rate=5),
                // エリア18
                mLocationSpawnEntity(location_id=18, pokemon_id=950, pokemon_name="ヤヤコマ", spawn_rate=30),
                mLocationSpawnEntity(location_id=18, pokemon_id=510, pokemon_name="シママ", spawn_rate=15),
                mLocationSpawnEntity(location_id=18, pokemon_id=600, pokemon_name="キノココ", spawn_rate=20),
                mLocationSpawnEntity(location_id=18, pokemon_id=210, pokemon_name="フシデ", spawn_rate=25),
                mLocationSpawnEntity(location_id=18, pokemon_id=580, pokemon_name="ニャスパー", spawn_rate=10),
                // エリア19
                mLocationSpawnEntity(location_id=19, pokemon_id=950, pokemon_name="ヤヤコマ", spawn_rate=23),
                mLocationSpawnEntity(location_id=19, pokemon_id=960, pokemon_name="ヒノヤコマ", spawn_rate=5),
                mLocationSpawnEntity(location_id=19, pokemon_id=980, pokemon_name="コジョフー", spawn_rate=20),
                mLocationSpawnEntity(location_id=19, pokemon_id=1000, pokemon_name="カヌチャン", spawn_rate=15),
                mLocationSpawnEntity(location_id=19, pokemon_id=530, pokemon_name="モグリュー", spawn_rate=22),
                mLocationSpawnEntity(location_id=19, pokemon_id=1030, pokemon_name="グライガー", spawn_rate=10),
                mLocationSpawnEntity(location_id=19, pokemon_id=780, pokemon_name="カルボウ", spawn_rate=5),
                // エリア20
                mLocationSpawnEntity(location_id=20, pokemon_id=1070, pokemon_name="ヒトツキ", spawn_rate=98),
                mLocationSpawnEntity(location_id=20, pokemon_id=1080, pokemon_name="ニダンギル", spawn_rate=2),
                // エリア21
                mLocationSpawnEntity(location_id=21, pokemon_id=1100, pokemon_name="ムックル", spawn_rate=18),
                mLocationSpawnEntity(location_id=21, pokemon_id=1110, pokemon_name="ムクバード", spawn_rate=3),
                mLocationSpawnEntity(location_id=21, pokemon_id=362, pokemon_name="シキジカ秋", spawn_rate=18),
                mLocationSpawnEntity(location_id=21, pokemon_id=580, pokemon_name="ニャスパー", spawn_rate=10),
                mLocationSpawnEntity(location_id=21, pokemon_id=210, pokemon_name="フシデ", spawn_rate=10),
                mLocationSpawnEntity(location_id=21, pokemon_id=510, pokemon_name="シママ", spawn_rate=12),
                mLocationSpawnEntity(location_id=21, pokemon_id=520, pokemon_name="ゼブライカ", spawn_rate=3),
                mLocationSpawnEntity(location_id=21, pokemon_id=600, pokemon_name="キノココ", spawn_rate=16),
                mLocationSpawnEntity(location_id=21, pokemon_id=980, pokemon_name="コジョフー", spawn_rate=10),
                // エリア22
                mLocationSpawnEntity(location_id=22, pokemon_id=1100, pokemon_name="ムックル", spawn_rate=10),
                mLocationSpawnEntity(location_id=22, pokemon_id=1110, pokemon_name="ムクバード", spawn_rate=4),
                mLocationSpawnEntity(location_id=22, pokemon_id=580, pokemon_name="ニャスパー", spawn_rate=10),
                mLocationSpawnEntity(location_id=22, pokemon_id=1130, pokemon_name="ミブリム", spawn_rate=14),
                mLocationSpawnEntity(location_id=22, pokemon_id=1160, pokemon_name="ゾロア", spawn_rate=5),
                mLocationSpawnEntity(location_id=22, pokemon_id=1180, pokemon_name="ボクレー", spawn_rate=10),
                mLocationSpawnEntity(location_id=22, pokemon_id=210, pokemon_name="フシデ", spawn_rate=10),
                mLocationSpawnEntity(location_id=22, pokemon_id=220, pokemon_name="ホイーガ", spawn_rate=3),
                mLocationSpawnEntity(location_id=22, pokemon_id=230, pokemon_name="ペンドラー", spawn_rate=1),
                mLocationSpawnEntity(location_id=22, pokemon_id=600, pokemon_name="キノココ", spawn_rate=10),
                mLocationSpawnEntity(location_id=22, pokemon_id=610, pokemon_name="キノガッサ", spawn_rate=4),
                mLocationSpawnEntity(location_id=22, pokemon_id=670, pokemon_name="エモンガ", spawn_rate=5),
                mLocationSpawnEntity(location_id=22, pokemon_id=660, pokemon_name="パチリス", spawn_rate=5),
                mLocationSpawnEntity(location_id=22, pokemon_id=640, pokemon_name="チュリネ", spawn_rate=7),
                mLocationSpawnEntity(location_id=22, pokemon_id=651, pokemon_name="ドレディアH", spawn_rate=2),
                // エリア23
                mLocationSpawnEntity(location_id=23, pokemon_id=1200, pokemon_name="ヒトモシ", spawn_rate=90),
                mLocationSpawnEntity(location_id=23, pokemon_id=1230, pokemon_name="クレッフィ", spawn_rate=10),
                // エリア24
                mLocationSpawnEntity(location_id=24, pokemon_id=1100, pokemon_name="ムックル", spawn_rate=16),
                mLocationSpawnEntity(location_id=24, pokemon_id=1110, pokemon_name="ムクバード", spawn_rate=5),
                mLocationSpawnEntity(location_id=24, pokemon_id=1240, pokemon_name="ヌメラ", spawn_rate=17),
                mLocationSpawnEntity(location_id=24, pokemon_id=1250, pokemon_name="ヌメイルH", spawn_rate=2),
                mLocationSpawnEntity(location_id=24, pokemon_id=490, pokemon_name="ズピカ", spawn_rate=16),
                mLocationSpawnEntity(location_id=24, pokemon_id=280, pokemon_name="マリル", spawn_rate=17),
                mLocationSpawnEntity(location_id=24, pokemon_id=290, pokemon_name="マリルリ", spawn_rate=5),
                mLocationSpawnEntity(location_id=24, pokemon_id=1270, pokemon_name="ミニリュウ", spawn_rate=17),
                mLocationSpawnEntity(location_id=24, pokemon_id=930, pokemon_name="ミガルーサ", spawn_rate=5),
                // エリア25
                mLocationSpawnEntity(location_id=25, pokemon_id=1300, pokemon_name="ロコンA", spawn_rate=25),
                mLocationSpawnEntity(location_id=25, pokemon_id=363, pokemon_name="シキジカ冬", spawn_rate=25),
                mLocationSpawnEntity(location_id=25, pokemon_id=980, pokemon_name="コジョフー", spawn_rate=10),
                mLocationSpawnEntity(location_id=25, pokemon_id=1100, pokemon_name="ムックル", spawn_rate=25),
                mLocationSpawnEntity(location_id=25, pokemon_id=1110, pokemon_name="ムクバード", spawn_rate=5),
                mLocationSpawnEntity(location_id=25, pokemon_id=1340, pokemon_name="リオル", spawn_rate=5),
                mLocationSpawnEntity(location_id=25, pokemon_id=1161, pokemon_name="ゾロアH", spawn_rate=5),
                // エリア26
                mLocationSpawnEntity(location_id=26, pokemon_id=980, pokemon_name="コジョフー", spawn_rate=12),
                mLocationSpawnEntity(location_id=26, pokemon_id=1030, pokemon_name="グライガー", spawn_rate=10),
                mLocationSpawnEntity(location_id=26, pokemon_id=210, pokemon_name="フシデ", spawn_rate=8),
                mLocationSpawnEntity(location_id=26, pokemon_id=220, pokemon_name="ホイーガ", spawn_rate=3),
                mLocationSpawnEntity(location_id=26, pokemon_id=230, pokemon_name="ペンドラー", spawn_rate=1),
                mLocationSpawnEntity(location_id=26, pokemon_id=960, pokemon_name="ヒノヤコマ", spawn_rate=9),
                mLocationSpawnEntity(location_id=26, pokemon_id=970, pokemon_name="ファイアロー", spawn_rate=1),
                mLocationSpawnEntity(location_id=26, pokemon_id=1000, pokemon_name="カヌチャン", spawn_rate=12),
                mLocationSpawnEntity(location_id=26, pokemon_id=1010, pokemon_name="ナカヌチャン", spawn_rate=2),
                mLocationSpawnEntity(location_id=26, pokemon_id=530, pokemon_name="モグリュー", spawn_rate=12),
                mLocationSpawnEntity(location_id=26, pokemon_id=540, pokemon_name="ドリュウズ", spawn_rate=2),
                mLocationSpawnEntity(location_id=26, pokemon_id=550, pokemon_name="ユニラン", spawn_rate=10),
                mLocationSpawnEntity(location_id=26, pokemon_id=560, pokemon_name="ダブラン", spawn_rate=2),
                mLocationSpawnEntity(location_id=26, pokemon_id=1360, pokemon_name="アイアント", spawn_rate=10),
                mLocationSpawnEntity(location_id=26, pokemon_id=1370, pokemon_name="モノズ", spawn_rate=5),
                mLocationSpawnEntity(location_id=26, pokemon_id=1380, pokemon_name="ジヘッド", spawn_rate=1),
                // エリア27
                mLocationSpawnEntity(location_id=27, pokemon_id=980, pokemon_name="コジョフー", spawn_rate=19),
                mLocationSpawnEntity(location_id=27, pokemon_id=530, pokemon_name="モグリュー", spawn_rate=19),
                mLocationSpawnEntity(location_id=27, pokemon_id=540, pokemon_name="ドリュウズ", spawn_rate=3),
                mLocationSpawnEntity(location_id=27, pokemon_id=960, pokemon_name="ヒノヤコマ", spawn_rate=19),
                mLocationSpawnEntity(location_id=27, pokemon_id=780, pokemon_name="カルボウ", spawn_rate=15),
                mLocationSpawnEntity(location_id=27, pokemon_id=1301, pokemon_name="ロコン", spawn_rate=20),
                mLocationSpawnEntity(location_id=27, pokemon_id=1400, pokemon_name="メラルバ", spawn_rate=5)
            ).forEach { mLocationSpawnDao.insertSpawn(it) }

            // ==========================================
            // ★ ポケモンマスターデータ（別ファイルから読み込み）
            // ==========================================
            PokemonSeedData.getPokemons().forEach {
                mPokemonDao.insertPokemon(it)
            }
        }
    }
}