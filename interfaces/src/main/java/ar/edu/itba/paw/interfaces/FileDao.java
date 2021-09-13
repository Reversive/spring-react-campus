package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.FileCategory;
import ar.edu.itba.paw.models.FileModel;


import java.util.List;
import java.util.Optional;

public interface FileDao {
    FileModel create(FileModel file);
    boolean update(long fileId, FileModel file);
    boolean delete(long fileId);
    List<FileModel> list();
    Optional<FileModel> getById(long fileId);

    List<FileModel> getByName(String fileName);
    List<FileModel> getByExtension(long extensionId);
    List<FileModel> getByExtension(String extension);

    boolean addCategory(long fileId, long fileCategoryId);
    boolean removeCategory(long fileId, long fileCategoryId);
    List<FileCategory> getFileCategories(long fileId);
    List<FileModel> getByCategory(long fileCategoryId);

    List<FileModel> getByCourseId(long courseId);
}
