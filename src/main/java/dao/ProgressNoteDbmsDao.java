package dao;

import model.ProgressNote;
import utils.DbsConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProgressNoteDbmsDao  implements ProgressNoteDao{
    private static ProgressNoteDbmsDao instance;
    private final List<ProgressNote> progressNoteList = new ArrayList<>();

    public static synchronized ProgressNoteDbmsDao getInstance(){
        if(instance == null){
            instance = new ProgressNoteDbmsDao();
        }
        return instance;
    }


    @Override
    public ProgressNote selectProgressNoteById(String id) {
        for (ProgressNote progressNote : this.progressNoteList) {
            if (progressNote.getId().equals(id)) {
                return progressNote;
            }
        }

        String query = "SELECT id, commentProgress, date " +
                "FROM progressNotes " +
                "WHERE id = ?";

        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String progressNoteId = resultSet.getString("id");
                    String commentProgress = resultSet.getString("commentProgress");
                    LocalDate date = resultSet.getDate("date").toLocalDate();

                    ProgressNote progressNote = new ProgressNote(progressNoteId, commentProgress, date);
                    this.progressNoteList.add(progressNote);
                    return progressNote;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void saveProgressNote(ProgressNote progressNote, String customOrderId) {
        for (ProgressNote note : this.progressNoteList) {
            if (note.getId().equals(progressNote.getId())) {
                return;
            }
        }

        String query = "INSERT INTO progressNotes (id, commentProgress, date, orderId) " +
                "VALUES (?, ?, ?, ?)";

        Connection connection = DbsConnector.getInstance().getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, progressNote.getId());
            preparedStatement.setString(2, progressNote.getComment());
            preparedStatement.setDate(3, java.sql.Date.valueOf(progressNote.getDate()));
            preparedStatement.setString(4, customOrderId);
            preparedStatement.executeUpdate();

            this.progressNoteList.add(progressNote);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
