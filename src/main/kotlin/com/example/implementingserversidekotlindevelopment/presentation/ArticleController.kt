package com.example.implementingserversidekotlindevelopment.presentation

// 下記の package は OpenAPI Generator によって生成された package
import com.example.implementingserversidekotlindevelopment.openapi.generated.controller.ArticlesApi
import com.example.implementingserversidekotlindevelopment.openapi.generated.model.Article
import com.example.implementingserversidekotlindevelopment.openapi.generated.model.GenericErrorModel
import com.example.implementingserversidekotlindevelopment.openapi.generated.model.GenericErrorModelErrors
import com.example.implementingserversidekotlindevelopment.openapi.generated.model.SingleArticleResponse
import com.example.implementingserversidekotlindevelopment.usecase.ShowArticleUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
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
            { throw ShowArticleUseCaseErrorException(it) },
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

/**
 * 単一記事取得ユースケースがエラーを戻した時の Exception
 *
 * このクラスの例外が発生した時に、@ExceptionHandler で例外処理を行う
 *
 * @property error
 */
data class ShowArticleUseCaseErrorException(val error: ShowArticleUseCase.Error) : Exception()

/**
 * ShowArticleUseCaseErrorException をハンドリングする関数
 *
 * ShowArticleUseCase.Error の型に合わせてレスポンスを分岐させる
 */
@ExceptionHandler(value = [ShowArticleUseCaseErrorException::class])
fun onShowArticleUseCaseErrorException(e: ShowArticleUseCaseErrorException): ResponseEntity<GenericErrorModel> =
    when (val error = e.error) {
        /**
         * 原因: slug に該当する記事が見つからなかった
         */
        is ShowArticleUseCase.Error.NotFoundArticleBySlug -> ResponseEntity<GenericErrorModel>(
            GenericErrorModel(
                errors = GenericErrorModelErrors(
                    body = listOf("${error.slug.value} に該当する記事は見つかりませんでした")
                )
            ),
            HttpStatus.NOT_FOUND
        )

        /**
         * 原因: バリデーションエラー
         */
        is ShowArticleUseCase.Error.ValidationErrors -> ResponseEntity<GenericErrorModel>(
            GenericErrorModel(
                errors = GenericErrorModelErrors(
                    body = error.errors.map { it.message }
                )
            ),
            HttpStatus.FORBIDDEN
        )
    }
