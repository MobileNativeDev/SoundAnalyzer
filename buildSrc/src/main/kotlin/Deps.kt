import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.dependencies

object Deps {

    object Versions {

        const val androidxCoreKtx = "1.7.0"
        const val appcompat = "1.4.1"
        const val material = "1.5.0"
        const val constraintLayout = "2.1.3"
        const val jUnit = "4.13.2"
        const val androidJunit = "1.1.3"
        const val espresso = "3.4.0"
        const val coroutinesAndroid = "1.6.0"
        const val viewModelKtx = "2.4.0"
        const val koin = "3.1.5"

    }

    private const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.androidxCoreKtx}"
    private const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    private const val material = "com.google.android.material:material:${Versions.material}"
    private const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    private const val jUnit = "junit:junit:${Versions.jUnit}"
    private const val androidJunit = "androidx.test.ext:junit:${Versions.androidJunit}"
    private const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    private const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesAndroid}"
    private const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewModelKtx}"
    private const val koin = "io.insert-koin:koin-android:${Versions.koin}"

    fun DependencyHandlerScope.implementation(library: String) {
        add("implementation", library)
    }

    fun DependencyHandlerScope.testImplementation(library: String) {
        add("testImplementation", library)
    }

    fun DependencyHandlerScope.androidTestImplementation(library: String) {
        add("androidTestImplementation", library)
    }

    fun Project.defaultDependencies(additional: (DependencyHandlerScope.() -> Unit)? = null) {
        dependencies {
            implementAndroid()
            implementTesting()
            additional?.invoke(this)
        }
    }

    fun DependencyHandlerScope.implementAndroid() {
        implementation(androidxCoreKtx)
        implementation(appcompat)
        implementation(material)
        implementation(constraintLayout)
        implementation(coroutinesAndroid)
        implementation(viewModelKtx)
        implementation(koin)
    }

    fun DependencyHandlerScope.implementTesting() {
        testImplementation(jUnit)
        androidTestImplementation(androidJunit)
        androidTestImplementation(espresso)
    }

}