package com.apero.composetraining.session3.exercises

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.R
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐ BÀI TẬP 4: Multi-step Registration Form (Advanced)
 *
 * Mô tả: Form đăng ký nhiều bước với complex state management, validation, UDF pattern
 *
 * Steps: Personal Info → Contact → Preferences → Review
 *
 * Key concepts:
 * - @Stable annotation: đánh dấu class "ổn định" → Compose SKIP recompose nếu params không đổi
 * - UDF (Unidirectional Data Flow): State đi xuống, Events đi lên
 * - sealed class FormAction: type-safe events thay vì nhiều callbacks
 * - AnimatedContent: slide animation khi chuyển step
 * - rememberSaveable + custom Saver: form state survive xoay màn hình
 *
 * Bonus (nếu xong sớm):
 * - TODO: [Bonus] Implement custom Saver cho FormState (dùng mapSaver hoặc listSaver)
 * - val formState = rememberSaveable(saver = FormStateSaver) { FormState() }
 */

// ─── State & Actions (UDF pattern) ───────────────────────────────────────────

/**
 * @Stable annotation — tại sao cần?
 *
 * @Stable nói với Compose rằng:
 * 1. Nếu các properties không đổi (theo equals()), class được xem là "stable"
 * 2. Compose CÓ THỂ SKIP recompose nếu toàn bộ params không thay đổi
 *
 * @Stable vs @Immutable:
 * - @Stable: properties có thể thay đổi NHƯNG theo equals() đúng cách
 * - @Immutable: properties KHÔNG BAO GIỜ thay đổi (mạnh hơn @Stable)
 */
@Stable
data class FormState(
    // Step 1: Personal Info
    val firstName: String = "",
    val lastName: String = "",
    val birthYear: String = "",

    // Step 2: Contact
    val email: String = "",
    val phone: String = "",
    val city: String = "",

    // Step 3: Preferences
    val receiveNewsletter: Boolean = false,
    val receiveNotifications: Boolean = true,
    val preferredLanguage: String = "Vietnamese",

    // Navigation
    val currentStep: Int = 0,

    // Validation errors
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,

    // Submission
    val isSubmitted: Boolean = false,
)

val FormState.totalSteps: Int get() = 4
val FormState.progress: Float get() = (currentStep + 1).toFloat() / totalSteps.toFloat()
val FormState.stepTitle: String
    get() = when (currentStep) {
        0 -> "Personal Info"
        1 -> "Contact Details"
        2 -> "Preferences"
        3 -> "Review & Submit"
        else -> ""
    }

/**
 * sealed class FormAction — type-safe events từ UI lên ViewModel/Host
 *
 * Thay vì nhiều callbacks rời rạc (onFirstNameChange, onNext, onSubmit...)
 * → Dùng 1 callback duy nhất: onAction: (FormAction) -> Unit
 *
 * Lợi ích: API gọn hơn, dễ log, dễ test
 */
sealed class FormAction {
    data class UpdateFirstName(val value: String) : FormAction()
    data class UpdateLastName(val value: String) : FormAction()
    data class UpdateBirthYear(val value: String) : FormAction()
    data class UpdateEmail(val value: String) : FormAction()
    data class UpdatePhone(val value: String) : FormAction()
    data class UpdateCity(val value: String) : FormAction()
    data class UpdateNewsletter(val enabled: Boolean) : FormAction()
    data class UpdateNotifications(val enabled: Boolean) : FormAction()
    data class UpdateLanguage(val language: String) : FormAction()
    data object NextStep : FormAction()
    data object PrevStep : FormAction()
    data object Submit : FormAction()
}

// ─── Business Logic (Reducer) ─────────────────────────────────────────────────

/**
 * Hàm reduce: nhận state hiện tại + action → trả về state mới
 *
 * Pattern: Pure function, không có side effects
 * - Input: (FormState, FormAction) → Output: FormState
 * - Dễ test: chỉ cần verify output state
 */
fun reduceFormState(state: FormState, action: FormAction): FormState {
    return when (action) {
        is FormAction.UpdateFirstName -> state.copy(firstName = action.value, firstNameError = null)
        is FormAction.UpdateLastName -> state.copy(lastName = action.value, lastNameError = null)
        is FormAction.UpdateBirthYear -> state.copy(birthYear = action.value)
        is FormAction.UpdateEmail -> state.copy(email = action.value, emailError = null)
        is FormAction.UpdatePhone -> state.copy(phone = action.value, phoneError = null)
        is FormAction.UpdateCity -> state.copy(city = action.value)
        is FormAction.UpdateNewsletter -> state.copy(receiveNewsletter = action.enabled)
        is FormAction.UpdateNotifications -> state.copy(receiveNotifications = action.enabled)
        is FormAction.UpdateLanguage -> state.copy(preferredLanguage = action.language)

        is FormAction.NextStep -> {
            val validatedState = validateCurrentStep(state)
            if (!validatedState.hasCurrentStepErrors) {
                validatedState.copy(currentStep = (state.currentStep + 1).coerceAtMost(state.totalSteps - 1))
            } else {
                validatedState
            }
        }

        is FormAction.PrevStep -> {
            state.copy(currentStep = (state.currentStep - 1).coerceAtLeast(0))
        }

        is FormAction.Submit -> {
            val validatedState = validateCurrentStep(state)
            if (!validatedState.hasCurrentStepErrors) {
                validatedState.copy(isSubmitted = true)
            } else {
                validatedState
            }
        }
    }
}

private fun validateCurrentStep(state: FormState): FormState {
    return when (state.currentStep) {
        0 -> {
            val firstNameError = if (state.firstName.isBlank()) "Không được để trống" else null
            val lastNameError = if (state.lastName.isBlank()) "Không được để trống" else null
            state.copy(
                firstNameError = firstNameError,
                lastNameError = lastNameError
            )
        }

        1 -> {
            val emailError = if (!state.email.contains("@")) "Email không đúng định dạng" else null
            val phoneError =
                if (state.phone.length < 9) "Số điện thoại phải có ít nhất 9 số" else null
            state.copy(
                emailError = emailError,
                phoneError = phoneError
            )
        }

        else -> state
    }
}

private val FormState.hasCurrentStepErrors: Boolean
    get() = when (currentStep) {
        0 -> firstNameError != null || lastNameError != null
        1 -> emailError != null || phoneError != null
        else -> false
    }

// ─── Host Composable (Stateful) ───────────────────────────────────────────────

/**
 * MultiStepFormScreen — stateful host
 *
 * Host giữ state và cung cấp cho FormContent (stateless child)
 * Pattern: State hosting ở level cao nhất cần dùng state
 */
@Composable
fun MultiStepFormScreen(modifier: Modifier = Modifier) {
    var formState by remember { mutableStateOf(FormState()) }
    val onAction: (FormAction) -> Unit = { action ->
        formState = reduceFormState(formState, action)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.bg_page))
    ) {
        if (formState.isSubmitted) {
            SubmissionSuccessScreen(formState = formState)
        } else {
            FormContent(
                state = formState,
                onAction = onAction,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(R.color.bg_page))
            )
        }
    }
}

// ─── Stateless Form Content (UDF Consumer) ───────────────────────────────────

/**
 * FormContent — stateless, nhận state + onAction
 *
 * Đây là điểm áp dụng UDF:
 * - state goes down (nhận từ host)
 * - events go up (gửi onAction lên host)
 */
@Composable
private fun FormContent(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            FormNavigationButtons(state = state, onAction = onAction)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(colorResource(R.color.bg_page))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                FormHeader(state)
                Spacer(modifier = Modifier.size(16.dp))
                LinearProgressIndicator(
                    progress = { state.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp),
                    color = colorResource(R.color.primary_blue),
                    trackColor = colorResource(R.color.bg_muted)
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    text = "Bước ${state.currentStep + 1} / ${state.totalSteps} — ${state.stepTitle}",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.outfit_font)),
                    color = colorResource(R.color.text_tertiary),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.size(24.dp))
                AnimatedContent(
                    targetState = state.currentStep,
                    transitionSpec = { // slide từ phải vào nếu đi tới, từ trái vào nếu đi lùi
                        val direction = if (targetState > initialState) 1 else -1
                        slideInHorizontally { it * direction } togetherWith slideOutHorizontally { it * -direction }
                    },
                    modifier = Modifier.weight(1f)
                ) { step ->
                    when (step) {
                        0 -> PersonalInfoStep(
                            state = state,
                            onAction = onAction
                        )

                        1 -> ContactStep(
                            state = state,
                            onAction = onAction
                        )

                        2 -> PreferencesStep(
                            state = state,
                            onAction = onAction
                        )

                        3 -> ReviewStep(state = state)
                    }
                }
            }
        }
    }
}

// ─── Form Header ──────────────────────────────────────────────────────────────

@Composable
private fun FormHeader(
    state: FormState,
    modifier: Modifier = Modifier,
) {
    Box {
        Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = colorResource(R.color.text_primary)
            )
            Text(
                text = "Đăng Ký", fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily(Font(R.font.outfit_font)),
                color = colorResource(R.color.text_primary),
                modifier = Modifier.padding(start = 15.dp)
            )
        }
    }
}

// ─── Step 1: Personal Info ────────────────────────────────────────────────────

@Composable
private fun PersonalInfoStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ValidatedTextField(
            value = state.firstName,
            onValueChange = { onAction(FormAction.UpdateFirstName(it)) },
            label = "First Name",
            errorMessage = state.firstNameError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    null,
                    tint = Color.Black
                )
            }
        )
        ValidatedTextField(
            value = state.lastName,
            onValueChange = { onAction(FormAction.UpdateLastName(it)) },
            label = "Last Name",
            errorMessage = state.lastNameError,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    null,
                    tint = Color.Black
                )
            }
        )
        ValidatedTextField(
            value = state.birthYear,
            onValueChange = { onAction(FormAction.UpdateBirthYear(it)) },
            label = "Birth Year",
            errorMessage = null,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Cake,
                    null,
                    tint = Color.Black
                )
            },
            keyboardType = KeyboardType.Number
        )
    }
}

// ─── Step 2: Contact ──────────────────────────────────────────────────────────

@Composable
private fun ContactStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.verticalScroll(scrollState)) {
        ValidatedTextField(
            value = state.email,
            onValueChange = { onAction(FormAction.UpdateEmail(it)) },
            label = "Email",
            errorMessage = state.emailError,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Email, null)
            },
            keyboardType = KeyboardType.Email
        )
        ValidatedTextField(
            value = state.phone,
            onValueChange = { onAction(FormAction.UpdatePhone(it)) },
            label = "Phone",
            errorMessage = state.phoneError,
            leadingIcon = {
                Icon(imageVector = Icons.Default.Phone, null)
            },
            keyboardType = KeyboardType.Phone
        )
        ValidatedTextField(
            value = state.city,
            onValueChange = { onAction(FormAction.UpdateCity(it)) },
            label = "City",
            errorMessage = null,
            leadingIcon = {
                Icon(imageVector = Icons.Default.LocationCity, null)
            }
        )
    }
}

// ─── Step 3: Preferences ─────────────────────────────────────────────────────

@Composable
private fun PreferencesStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SwitchRow(
            label = "Receive newsletter",
            checked = state.receiveNewsletter,
            onCheckedChange = { onAction(FormAction.UpdateNewsletter(it)) }
        )
        SwitchRow(
            label = "Push notifications",
            checked = state.receiveNotifications,
            onCheckedChange = { onAction(FormAction.UpdateNotifications(it)) }
        )
        HorizontalDivider()
        Text(
            text = "Preferred language",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Vietnamese", "English", "Japanese", "Korean").forEach { lang ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = state.preferredLanguage == lang,
                        onClick = { onAction(FormAction.UpdateLanguage(lang)) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = colorResource(R.color.primary_blue),
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = lang,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

// ─── Step 4: Review ───────────────────────────────────────────────────────────

@Composable
private fun ReviewStep(
    state: FormState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Review your information",
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.text_primary)
        )

        ReviewSection("Personal Info") {
            ReviewRow("Name", "${state.firstName} ${state.lastName}")
            ReviewRow("Birth Year", state.birthYear)
        }

        ReviewSection("Contact") {
            ReviewRow("Email", state.email)
            ReviewRow("Phone", state.phone)
            ReviewRow("City", state.city)
        }

        ReviewSection("Preferences") {
            ReviewRow("Newsletter", if (state.receiveNewsletter) "Yes" else "No")
            ReviewRow("Notifications", if (state.receiveNotifications) "Yes" else "No")
            ReviewRow("Language", state.preferredLanguage)
        }

        Text(
            text = "Nhấn Submit để hoàn tất",
            style = MaterialTheme.typography.bodySmall,
            color = Color.DarkGray
        )
    }
}

@Composable
private fun ReviewSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
}

@Composable
private fun ReviewRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

// ─── Navigation Buttons ───────────────────────────────────────────────────────

@Composable
private fun FormNavigationButtons(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLastStep = state.currentStep == state.totalSteps - 1

    Surface(
        color = Color.White,
        modifier = modifier.shadow(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.currentStep > 0) {
                OutlinedButton(
                    onClick = { onAction(FormAction.PrevStep) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(
                        width = 1.dp,
                        colorResource(R.color.border_strong)
                    ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = colorResource(R.color.text_secondary)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Trước")
                }
            }

            Button(
                onClick = {
                    if (isLastStep) {
                        onAction(FormAction.Submit)
                    } else {
                        onAction(FormAction.NextStep)
                    }
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.primary_blue),
                    contentColor = Color.White
                )
            ) {
                Text(text = if (isLastStep) "Submit" else "Sau")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (isLastStep) Icons.Default.Check else Icons.Default.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

// ─── Shared Components ────────────────────────────────────────────────────────

@Composable
private fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    leadingIcon: @Composable (() -> Unit),
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val isError = !errorMessage.isNullOrBlank()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(text = label, color = Color.DarkGray)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = label, color = Color.DarkGray.copy(alpha = 0.4f))
            },
            leadingIcon = leadingIcon,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(text = errorMessage, color = Color.Red)
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                errorBorderColor = colorResource(R.color.error_red),
                errorContainerColor = colorResource(R.color.error_red_light),
                errorLeadingIconColor = colorResource(R.color.error_red),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                errorTextColor = colorResource(R.color.error_red),
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black.copy(alpha = 0.4f),
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = colorResource(R.color.primary_blue),
                checkedBorderColor = Color.Transparent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = colorResource(R.color.uncheck_track),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

// ─── Success Screen ───────────────────────────────────────────────────────────

@Composable
private fun SubmissionSuccessScreen(
    formState: FormState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Registration Complete!",
            style = MaterialTheme.typography.headlineMedium,
            color = colorResource(R.color.primary_blue).copy(alpha = 5f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome, ${formState.firstName} ${formState.lastName}!",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Black.copy(alpha = 0.5f)
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Multi Step Form - Light")
@Composable
private fun MultiStepFormPreview() {
    AppTheme {
        MultiStepFormScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Multi Step Form - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun MultiStepFormDarkPreview() {
    AppTheme(darkTheme = true) {
        MultiStepFormScreen()
    }
}

@Preview(showBackground = true, name = "Review Step Preview")
@Composable
private fun ReviewStepPreview() {
    AppTheme {
        val sampleState = FormState(
            firstName = "John",
            lastName = "Doe",
            birthYear = "1995",
            email = "john@example.com",
            phone = "0901234567",
            city = "Ho Chi Minh City",
            receiveNewsletter = true,
            receiveNotifications = true,
            preferredLanguage = "Vietnamese",
            currentStep = 1,
        )
        FormContent(state = sampleState, onAction = {})
    }
}

@Preview(showBackground = true, name = "Success Screen Preview")
@Composable
private fun SuccessScreenPreview() {
    AppTheme {
        SubmissionSuccessScreen(
            formState = FormState(firstName = "John", lastName = "Doe"),
        )
    }
}

@Preview
@Composable
private fun HeaderPreview() {
    val sampleState = FormState(
        firstName = "John",
        lastName = "Doe",
        birthYear = "1995",
        email = "john@example.com",
        phone = "0901234567",
        city = "Ho Chi Minh City",
        receiveNewsletter = true,
        receiveNotifications = true,
        preferredLanguage = "Vietnamese",
        currentStep = 0,
    )
    FormHeader(sampleState)
}

@Preview(showBackground = true, name = "Validated Text Field - No Error")
@Composable
private fun ValidatedTextFieldPreview() {
    AppTheme {
        ValidatedTextField(
            value = "John Doe",
            onValueChange = {},
            label = "First Name",
            errorMessage = null,
            leadingIcon = { Icon(imageVector = Icons.Default.Check, contentDescription = null) }
        )
    }
}

@Preview(showBackground = true, name = "Validated Text Field - Error")
@Composable
private fun ValidatedTextFieldErrorPreview() {
    AppTheme {
        ValidatedTextField(
            value = "aa",
            onValueChange = {},
            label = "First Name",
            errorMessage = "First name is required",
            leadingIcon = { Icon(imageVector = Icons.Default.Check, contentDescription = null) }
        )
    }
}

@Preview(showBackground = true, name = "Personal Info Step Preview")
@Composable
private fun PersonalInfoStepPreview() {
    AppTheme {
        val sampleState = FormState(
            firstName = "John",
            lastName = "Doe",
            birthYear = "1995"
        )
        PersonalInfoStep(
            state = sampleState,
            onAction = {}
        )
    }
}

@Preview(showBackground = true, name = "Contact Step Preview")
@Composable
private fun ContactStepPreview() {
    AppTheme {
        val sampleState = FormState(
            email = "john.doe@example.com",
            phone = "0123456789",
            city = "Ho Chi Minh City"
        )
        ContactStep(
            state = sampleState,
            onAction = {}
        )
    }
}
