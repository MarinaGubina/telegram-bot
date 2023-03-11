package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.Task;
import pro.sky.telegrambot.repository.TaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final TaskRepository taskRepository;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, TaskRepository taskRepository) {
        this.telegramBot = telegramBot;
        this.taskRepository = taskRepository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            Long chatId = update.message().chat().id();

            if(update.message().text().equals("/start")){
                SendMessage message = new SendMessage(chatId, "Привет! Я - телеграм-бот, который поможет тебе " +
                        "спланировать дела. Введите напоминание в формате : " +
                        "'dd.MM.yyyy HH:mm Описание'");
                telegramBot.execute(message);
            }
            else{
                String str = update.message().text();
                Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
                Matcher matcher = pattern.matcher(str);
                if(matcher.matches()){
                    String date = matcher.group(1);
                    String item = matcher.group(3);
                    LocalDateTime time = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    Task task = new Task();
                    task.setChatId(chatId);
                    task.setDescription(item);
                    task.setTime(time);
                    taskRepository.save(task);
                    SendMessage message2 = new SendMessage(chatId, "Задача " + task + " запланирована");
                    telegramBot.execute(message2);
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
