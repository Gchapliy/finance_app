package com.example.myapplication.core.data.repository

import com.example.myapplication.core.data.dao.TransactionCategoryDao
import com.example.myapplication.core.domain.model.TransactionCategory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionExpenseCategoryRepository @Inject constructor (
    private val dao: TransactionCategoryDao
) {

     fun getCategories(): Flow<List<TransactionCategory>> = dao.getAll()

     suspend fun addCategory(category: TransactionCategory) {
         dao.insert(category)
     }
}