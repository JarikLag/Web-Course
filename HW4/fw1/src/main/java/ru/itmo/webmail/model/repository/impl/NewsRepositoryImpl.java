package ru.itmo.webmail.model.repository.impl;

import ru.itmo.webmail.model.domain.News;
import ru.itmo.webmail.model.repository.NewsRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NewsRepositoryImpl implements NewsRepository {
    private static final File tmpDir = new File(System.getProperty("java.io.tmpdir"));

    private List<News> newsList;

    public NewsRepositoryImpl() {
        try {
            //noinspection unchecked
            newsList = (List<News>) new ObjectInputStream(
                    new FileInputStream(new File(tmpDir, getClass().getSimpleName()))).readObject();
        } catch (Exception e) {
            newsList = new ArrayList<>();
        }
    }

    @Override
    public void save(News news) {
        newsList.add(news);

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    new FileOutputStream(new File(tmpDir, getClass().getSimpleName())));
            objectOutputStream.writeObject(newsList);
            objectOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException("Can't save news.", e);
        }
    }

    @Override
    public List<News> findAll() {
        return new ArrayList<>(newsList);
    }
}
