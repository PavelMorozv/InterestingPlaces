package ru.pavelmorozovtk.interestingplaces;

import java.util.List;

public interface IDatabaseHandler {
    public void addContact(Note note);
    public Note getContact(int id);
    public List<Note> getAllContacts();
    public int getContactsCount();
    public int updateContact(Note note);
    public void deleteContact(Note note);
    public void deleteAll();
}
