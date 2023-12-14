package com.example.implementingserversidekotlindevelopment.presentation

// 下記の package は OpenAPI Generator によって生成された package
import com.example.implementingserversidekotlindevelopment.openapi.generated.controller.ArticlesApi
import com.example.implementingserversidekotlindevelopment.openapi.generated.model.Article
import com.example.implementingserversidekotlindevelopment.openapi.generated.model.SingleArticleResponse
import com.example.implementingserversidekotlindevelopment.usecase.ShowArticleUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

/**
 * 作成済記事のコントローラー
 *
 * @property showArticleUseCase 単一記事取得ユースケース
 */
@RestController
class ArticleController(val showArticleUseCase: ShowArticleUseCase) : ArticlesApi {
    override fun getArticle(slug: String): ResponseEntity<SingleArticleResponse> {
        /**
         * 作成済記事の取得
         */
        val createdArticle = showArticleUseCase.execute(slug).fold(
            { throw TODO() }, // エラーハンドリングは後で実装
            { it }
        )

        return ResponseEntity(
            SingleArticleResponse(
                Article(
                    slug = createdArticle.slug.value,
                    title = createdArticle.title.value,
                    description = createdArticle.description.value,
                    body = createdArticle.body.value
                ),
            ),
            HttpStatus.OK
        )
    }
}
