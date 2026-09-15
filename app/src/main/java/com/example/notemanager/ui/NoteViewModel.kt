package com.example.notemanager.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notemanager.data.local.NoteEntity
import com.example.notemanager.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

sealed interface NoteUiEvent {
    data class SaveNote(val title: String, val content: String, val imageUri: String? = null) : NoteUiEvent
    data class ToggleComplete(val note: NoteEntity) : NoteUiEvent
    data class DeleteNote(val note: NoteEntity) : NoteUiEvent
}

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    val notesState: StateFlow<List<NoteEntity>> = repository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onEvent(event: NoteUiEvent) {
        viewModelScope.launch {
            when (event) {
                is NoteUiEvent.SaveNote -> {
                    if (event.title.isNotBlank() || event.content.isNotBlank()) {
                        repository.insertNote(
                            NoteEntity(
                                title = event.title,
                                content = event.content,
                                date = LocalDateTime.now(),
                                imageUri = event.imageUri
                            )
                        )
                    }
                }
                is NoteUiEvent.ToggleComplete -> {
                    repository.updateNote(event.note.copy(isCompleted = !event.note.isCompleted))
                }
                is NoteUiEvent.DeleteNote -> {
                    repository.deleteNote(event.note)
                }
            }
        }
    }
}
