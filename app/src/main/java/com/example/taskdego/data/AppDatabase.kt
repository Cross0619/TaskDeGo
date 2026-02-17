package com.example.taskdego.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.taskdego.data.dao.tTaskDao
import com.example.taskdego.data.dao.tTrainerProfileDao
import com.example.taskdego.data.dao.mItemDao
import com.example.taskdego.data.dao.tItemDao
import com.example.taskdego.data.entity.tTaskEntity
import com.example.taskdego.data.entity.tTrainerProfileEntity
import com.example.taskdego.data.entity.mItemEntity
import com.example.taskdego.data.entity.tItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        tTaskEntity::class,
        tTrainerProfileEntity::class,
        mItemEntity::class,
        tItemEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tTaskDao(): tTaskDao
    abstract fun tTrainerProfileDao(): tTrainerProfileDao
    abstract fun mItemDao(): mItemDao
    abstract fun tItemDao(): tItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                //開発中のみ、全部削除
                context.deleteDatabase("task_dego_database")

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

            // ==========================================
            // アイテムマスターデータの投入（全73種類）
            // ==========================================
            val items = listOf(
                // ボール類
                mItemEntity(m_item_id = 1, item_name = "モンスターボール", price = 500, is_pokemon_candy = false),
                mItemEntity(m_item_id = 2, item_name = "スーパーボール", price = 1500, is_pokemon_candy = false),
                mItemEntity(m_item_id = 3, item_name = "ハイパーボール", price = 3000, is_pokemon_candy = false),

                // きのみ・特殊アイテム
                mItemEntity(m_item_id = 4, item_name = "ズリのみ", price = null, is_pokemon_candy = false),
                mItemEntity(m_item_id = 5, item_name = "パイルのみ", price = null, is_pokemon_candy = false),
                mItemEntity(m_item_id = 6, item_name = "ふしぎなアメ", price = null, is_pokemon_candy = false),

                // ポケモンのアメ（第1-9世代）
                mItemEntity(m_item_id = 7, item_name = "ツタージャのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 8, item_name = "ポカブのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 9, item_name = "ミジュマルのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 10, item_name = "チラーミィのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 11, item_name = "マメパトのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 12, item_name = "コフキムシのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 13, item_name = "クルミルのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 14, item_name = "フシデのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 15, item_name = "ピチューのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 16, item_name = "ルリリのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 17, item_name = "トゲピーのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 18, item_name = "パモのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 19, item_name = "シキジカのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 20, item_name = "ヒメンカのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 21, item_name = "フラベベのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 22, item_name = "ニャオハのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 23, item_name = "ケロマツのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 24, item_name = "ズピカのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 25, item_name = "シママのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 26, item_name = "モグリューのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 27, item_name = "ユニランのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 28, item_name = "ニャスパーのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 29, item_name = "キノココのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 30, item_name = "モンメンのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 31, item_name = "チュリネのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 32, item_name = "パチリスのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 33, item_name = "エモンガのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 34, item_name = "ヘラクロスのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 35, item_name = "イーブイのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 36, item_name = "カルボウのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 37, item_name = "カイデンのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 38, item_name = "アシマリのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 39, item_name = "ケイコウオのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 40, item_name = "ウデッポウのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 41, item_name = "シビシラスのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 42, item_name = "ミガルーサのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 43, item_name = "ダダリンのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 44, item_name = "ヤヤコマのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 45, item_name = "コジョフーのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 46, item_name = "カヌチャンのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 47, item_name = "グライガーのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 48, item_name = "チゴラスのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 49, item_name = "ヒトツキのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 50, item_name = "ムックルのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 51, item_name = "ムクバードのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 52, item_name = "ミブリムのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 53, item_name = "ゾロアのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 54, item_name = "ボクレーのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 55, item_name = "ヒトモシのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 56, item_name = "クレッフィのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 57, item_name = "ヌメラのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 58, item_name = "ミニリュウのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 59, item_name = "ロコンのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 60, item_name = "アマルスのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 61, item_name = "リオルのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 62, item_name = "アイアントのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 63, item_name = "モノズのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 64, item_name = "メラルバのアメ", price = null, is_pokemon_candy = true),

                // 伝説・幻のポケモンのアメ
                mItemEntity(m_item_id = 65, item_name = "マッシブーンのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 66, item_name = "オーガポンのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 67, item_name = "ブリザポスのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 68, item_name = "レイスポスのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 69, item_name = "バドレックスのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 70, item_name = "カイオーガのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 71, item_name = "レシラムのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 72, item_name = "メロエッタのアメ", price = null, is_pokemon_candy = true),
                mItemEntity(m_item_id = 73, item_name = "アルセウスのアメ", price = null, is_pokemon_candy = true)
            )

            // 全アイテムをデータベースに挿入
            items.forEach { mItemDao.insertItem(it) }

            // ==========================================
            // トレーナー初期データ
            // ==========================================
            val trainer = tTrainerProfileEntity(
                t_trainer_id = 1,
                trainer_name = "たろう",
                level = 1,
                exp = 0,
                coins = 0,
                daily_reword_count_task = 0,
                daily_reword_count_routine = 0
            )
            tTrainerProfileDao.insertTrainer(trainer)
        }
    }
}