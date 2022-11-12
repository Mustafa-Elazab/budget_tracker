package com.example.budgettracker.model

import com.example.budgettracker.R

data class CategoryModel(var id: Int, var name: String, var iconId: Int)

class CategoryData {

    val transactionTags = listOf(
        "Housing",
        "Transportation",
        "Food",
        "Utilities",
        "Insurance",
        "Healthcare",
        "Saving & Debts",
        "Personal Spending",
        "Entertainment",
        "Miscellaneous"
    )

    companion object {
        var data = arrayListOf<CategoryModel>(
            CategoryModel(
                id = 11,
                name = "Housing",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 12,
                name = "Transportation",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 3,
                name = "Food",
                iconId = R.drawable.food
            ),
            CategoryModel(
                id = 4,
                name = "Coffer",
                iconId = R.drawable.coffee
            ),
            CategoryModel(
                id = 4,
                name = "Utilities",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 5,
                name = "Insurance",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 6,
                name = "Healthcare",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 7,
                name = "Saving & Debts",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 8,
                name = "Personal Spending",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 9,
                name = "Entertainment",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 10,
                name = "Miscellaneous",
                iconId = R.drawable.icon_bg
            ),
            CategoryModel(
                id = 1,
                name = "Transfer Balance",
                iconId = R.drawable.money_tansfer
            ),
            CategoryModel(
                id = 2,
                name = "Withdrawal Balance",
                iconId = R.drawable.money_tansfer
            ),
            CategoryModel(
                id = 13,
                name = "Other",
                iconId = R.drawable.money_tansfer
            ),


            )
    }
}