package com.kfokam48.evaluation.service;

import com.kfokam48.evaluation.domain.Etudiant;
import com.kfokam48.evaluation.domain.Exercice;
import com.kfokam48.evaluation.domain.Relecture;
import com.kfokam48.evaluation.domain.StatutRelecture;
import com.kfokam48.evaluation.dto.request.RendreRelectureRequest;
import com.kfokam48.evaluation.exception.AutoRelectureException;
import com.kfokam48.evaluation.exception.NoteInvalideException;
import com.kfokam48.evaluation.repository.ExerciceRepository;
import com.kfokam48.evaluation.repository.RelectureRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RelectureServiceTest {

    @Mock RelectureRepository relectureRepository;
    @Mock ExerciceRepository exerciceRepository;
    @InjectMocks RelectureService relectureService;

    @Test
    void refuse_note_invalide() {
        Relecture r = Relecture.builder().id(1L).build();
        when(relectureRepository.findById(1L)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> relectureService.rendre(1L,
                new RendreRelectureRequest(25, "trop haut")))
                .isInstanceOf(NoteInvalideException.class);
    }

    @Test
    void refuse_auto_relecture() {
        Etudiant etudiant = Etudiant.builder().id(7L).build();
        Exercice ex = Exercice.builder().id(1L).etudiant(etudiant).build();
        Relecture r = Relecture.builder()
                .id(1L).exercice(ex).relecteur(etudiant)
                .statut(StatutRelecture.EN_ATTENTE).build();
        when(relectureRepository.findById(1L)).thenReturn(Optional.of(r));

        assertThatThrownBy(() -> relectureService.rendre(1L,
                new RendreRelectureRequest(15, "ok")))
                .isInstanceOf(AutoRelectureException.class);
    }
}
