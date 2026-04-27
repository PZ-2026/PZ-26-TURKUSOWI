package com.turkusowi.animalsheltermenager.core.data

import com.google.gson.GsonBuilder
import com.turkusowi.animalsheltermenager.BuildConfig
import com.turkusowi.animalsheltermenager.features.admin.AdminDashboardSummary
import com.turkusowi.animalsheltermenager.features.admin.Employee
import com.turkusowi.animalsheltermenager.features.admin.EmployeeRole
import com.turkusowi.animalsheltermenager.features.admin.EmployeeStatus
import com.turkusowi.animalsheltermenager.features.admin.EmploymentType
import com.turkusowi.animalsheltermenager.features.animals.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate

data class SessionUser(
    val id: Int,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String,
    val isGuest: Boolean = false
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
}

data class WalkReservation(
    val id: Int,
    val volunteerId: Int,
    val animalId: Int,
    val animalName: String,
    val volunteerName: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val notes: String?,
    val location: String
)

data class LoginPayload(
    val email: String,
    val password: String
)

data class RegisterPayload(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)

private interface BackendApi {
    @POST("api/auth/login")
    suspend fun login(@Body payload: LoginRequestDto): AuthUserDto

    @POST("api/auth/register")
    suspend fun register(@Body payload: RegisterRequestDto): AuthUserDto

    @POST("api/auth/forgot-password")
    suspend fun forgotPassword(@Body payload: ForgotPasswordRequestDto): MessageResponseDto

    @GET("api/zwierzeta")
    suspend fun getAnimals(): List<AnimalDto>

    @GET("api/zwierzeta/{id}")
    suspend fun getAnimal(@Path("id") id: Int): AnimalDto

    @GET("api/uzytkownicy")
    suspend fun getUsers(
        @Query("aktywny") active: Boolean? = null
    ): List<UserDto>

    @GET("api/uzytkownicy/{id}")
    suspend fun getUser(@Path("id") id: Int): UserDto

    @PUT("api/uzytkownicy/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body payload: UpdateUserRequestDto): UserDto

    @GET("api/rezerwacje-spacerow")
    suspend fun getReservations(
        @Query("wolontariuszId") volunteerId: Int? = null,
        @Query("zwierzeId") animalId: Int? = null
    ): List<ReservationDto>

    @POST("api/rezerwacje-spacerow")
    suspend fun createReservation(@Body payload: CreateReservationRequestDto): ReservationDto

    @DELETE("api/rezerwacje-spacerow/{id}")
    suspend fun deleteReservation(@Path("id") id: Int)

    @GET("api/raporty-operacyjne")
    suspend fun getReports(): List<ReportDto>
}

private data class LoginRequestDto(val email: String, val password: String)
private data class RegisterRequestDto(val imie: String, val nazwisko: String, val email: String, val password: String)
private data class ForgotPasswordRequestDto(val email: String)
private data class AuthUserDto(val id: Int, val email: String, val imie: String, val nazwisko: String, val rola: String, val czyAktywny: Boolean)
private data class MessageResponseDto(val message: String)
private data class AnimalDto(
    val id: Int,
    val imie: String,
    val wiek: Int?,
    val waga: Double?,
    val plec: String?,
    val rasa: String?,
    val typZwierzecia: String?,
    val status: String?,
    val opis: String?,
    val charakter: String?
)
private data class UserDto(
    val id: Int,
    val email: String,
    val imie: String,
    val nazwisko: String,
    val rolaId: Int,
    val rolaNazwa: String,
    val czyAktywny: Boolean
)
private data class UpdateUserRequestDto(
    val email: String,
    val hasloHash: String,
    val imie: String,
    val nazwisko: String,
    val rolaId: Int,
    val czyAktywny: Boolean
)
private data class ReservationDto(
    val id: Int,
    val wolontariuszId: Int,
    val imieWolontariusza: String,
    val nazwiskoWolontariusza: String,
    val zwierzeId: Int,
    val imieZwierzecia: String,
    val dataSpaceru: String,
    val godzinaStart: String,
    val godzinaKoniec: String,
    val uwagi: String?
)
private data class CreateReservationRequestDto(
    val wolontariuszId: Int,
    val zwierzeId: Int,
    val dataSpaceru: String,
    val godzinaStart: String,
    val godzinaKoniec: String,
    val uwagi: String?
)
private data class ReportDto(val id: Int)

class SessionManager {
    private val _currentUser = MutableStateFlow<SessionUser?>(null)
    val currentUser: StateFlow<SessionUser?> = _currentUser.asStateFlow()

    fun login(user: SessionUser) {
        _currentUser.value = user
    }

    fun continueAsGuest() {
        _currentUser.value = SessionUser(
            id = BuildConfig.DEFAULT_GUEST_USER_ID,
            email = "guest@local",
            firstName = "Gosc",
            lastName = "",
            role = "GOSC",
            isGuest = true
        )
    }

    fun logout() {
        _currentUser.value = null
    }
}

class AppRepository {

    private val api: BackendApi by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val gson = GsonBuilder().create()

        Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BackendApi::class.java)
    }

    suspend fun login(payload: LoginPayload): SessionUser = withContext(Dispatchers.IO) {
        api.login(LoginRequestDto(payload.email.trim(), payload.password)).toSessionUser()
    }

    suspend fun register(payload: RegisterPayload): SessionUser = withContext(Dispatchers.IO) {
        api.register(
            RegisterRequestDto(
                imie = payload.firstName.trim(),
                nazwisko = payload.lastName.trim(),
                email = payload.email.trim(),
                password = payload.password
            )
        ).toSessionUser()
    }

    suspend fun requestPasswordReset(email: String): String = withContext(Dispatchers.IO) {
        api.forgotPassword(ForgotPasswordRequestDto(email.trim())).message
    }

    suspend fun getAnimals(): List<Animal> = withContext(Dispatchers.IO) {
        api.getAnimals().map { it.toAnimal() }
    }

    suspend fun getAnimal(id: Int): Animal = withContext(Dispatchers.IO) {
        api.getAnimal(id).toAnimal()
    }

    suspend fun getEmployees(activeOnly: Boolean = false): List<Employee> = withContext(Dispatchers.IO) {
        api.getUsers(active = if (activeOnly) true else null).map { it.toEmployee() }
    }

    suspend fun getEmployee(employeeId: Int): Employee = withContext(Dispatchers.IO) {
        api.getUser(employeeId).toEmployee()
    }

    suspend fun updateEmployee(employee: Employee): Employee = withContext(Dispatchers.IO) {
        api.updateUser(
            id = employee.id.toInt(),
            payload = UpdateUserRequestDto(
                email = employee.email,
                hasloHash = "temporary123",
                imie = employee.firstName,
                nazwisko = employee.lastName,
                rolaId = employee.role.toRoleId(),
                czyAktywny = employee.status == EmployeeStatus.ACTIVE
            )
        ).toEmployee()
    }

    suspend fun getReservations(volunteerId: Int? = null, animalId: Int? = null): List<WalkReservation> = withContext(Dispatchers.IO) {
        api.getReservations(volunteerId = volunteerId, animalId = animalId)
            .sortedBy { it.dataSpaceru + it.godzinaStart }
            .map { it.toReservation() }
    }

    suspend fun createWalkReservation(volunteerId: Int, animalId: Int, notes: String? = null): WalkReservation = withContext(Dispatchers.IO) {
        api.createReservation(
            CreateReservationRequestDto(
                wolontariuszId = volunteerId,
                zwierzeId = animalId,
                dataSpaceru = LocalDate.now().plusDays(1).toString(),
                godzinaStart = "10:00:00",
                godzinaKoniec = "11:00:00",
                uwagi = notes
            )
        ).toReservation()
    }

    suspend fun cancelReservation(reservationId: Int) = withContext(Dispatchers.IO) {
        api.deleteReservation(reservationId)
    }

    suspend fun getAdminDashboardSummary(): AdminDashboardSummary = withContext(Dispatchers.IO) {
        val accounts = api.getUsers().size
        val reports = api.getReports().size
        AdminDashboardSummary(
            managedAccounts = accounts,
            newReports = reports
        )
    }

    private fun AuthUserDto.toSessionUser(): SessionUser {
        return SessionUser(
            id = id,
            email = email,
            firstName = imie,
            lastName = nazwisko,
            role = rola
        )
    }

    private fun AnimalDto.toAnimal(): Animal {
        return Animal(
            id = id,
            name = imie,
            breed = rasa ?: "Nieznana rasa",
            animalType = typZwierzecia ?: "Zwierze",
            age = wiek?.let { "$it lata" } ?: "Brak danych",
            weight = waga?.let { "${it} kg" } ?: "Brak danych",
            gender = plec ?: "Brak danych",
            status = status ?: "Nieznany",
            description = opis ?: "Brak opisu.",
            temperament = charakter ?: "Brak danych"
        )
    }

    private fun UserDto.toEmployee(): Employee {
        return Employee(
            id = id.toString(),
            firstName = imie,
            lastName = nazwisko,
            email = email,
            role = rolaNazwa.toEmployeeRole(),
            status = if (czyAktywny) EmployeeStatus.ACTIVE else EmployeeStatus.BLOCKED,
            salaryPln = "0",
            employmentType = EmploymentType.FULL_TIME
        )
    }

    private fun ReservationDto.toReservation(): WalkReservation {
        return WalkReservation(
            id = id,
            volunteerId = wolontariuszId,
            animalId = zwierzeId,
            animalName = imieZwierzecia,
            volunteerName = "$imieWolontariusza $nazwiskoWolontariusza",
            date = dataSpaceru,
            startTime = godzinaStart.take(5),
            endTime = godzinaKoniec.take(5),
            notes = uwagi,
            location = "Schronisko - wybieg glowny"
        )
    }

    private fun String.toEmployeeRole(): EmployeeRole {
        return when (uppercase()) {
            "ADMIN" -> EmployeeRole.ADMIN
            "PRACOWNIK" -> EmployeeRole.SPECIALIST
            "WOLONTARIUSZ" -> EmployeeRole.CAREGIVER
            "SEKRETARZ" -> EmployeeRole.COORDINATOR
            else -> EmployeeRole.SPECIALIST
        }
    }

    private fun EmployeeRole.toRoleId(): Int {
        return when (this) {
            EmployeeRole.ADMIN -> 1
            EmployeeRole.SPECIALIST,
            EmployeeRole.SENIOR_SPECIALIST -> 2
            EmployeeRole.CAREGIVER -> 3
            EmployeeRole.COORDINATOR -> 4
        }
    }
}
