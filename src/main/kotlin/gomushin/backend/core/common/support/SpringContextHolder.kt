package gomushin.backend.core.common.support

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
object SpringContextHolder : ApplicationContextAware {
    lateinit var context: ApplicationContext

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    fun <T> getBean(clazz: Class<T>): T = context.getBean(clazz)
}
