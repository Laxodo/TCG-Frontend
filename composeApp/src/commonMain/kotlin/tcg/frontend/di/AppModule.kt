package tcg.frontend.di

import org.koin.dsl.module
import tcg.frontend.infraestructura.RestUserRepositorio
import tcg.frontend.modelo.IUserRepository

val moduloDominio = module {
    single<IUserRepository> { RestUserRepositorio("http://localhost:8080",get()) }
}