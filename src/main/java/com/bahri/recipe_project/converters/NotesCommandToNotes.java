package com.bahri.recipe_project.converters;

import com.bahri.recipe_project.commands.NotesCommand;
import com.bahri.recipe_project.domain.Note;

import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 6/21/17.
 */
@Component
public class NotesCommandToNotes implements Converter<NotesCommand, Note> {

    @Synchronized
    @Nullable
    @Override
    public Note convert(NotesCommand source) {
        if(source == null) {
            return null;
        }

        final Note notes = new Note();
        notes.setId(source.getId());
        notes.setRecipeNotes(source.getRecipeNotes());
        return notes;
    }
}
