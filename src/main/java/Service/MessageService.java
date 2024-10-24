package Service;

import java.util.List;

import DAO.AccountDao;
import DAO.MessageDao;
import Model.Message;

public class MessageService {
    private final MessageDao messageDao = new MessageDao();
    private final AccountDao accountDao = new AccountDao();

    /**
     * Persists the given message in the database
     * @param message the message to be persisted
     * @return the message with its generated id
     */
    public Message createMessage(Message message) {
        if (validateMessage(message)) {
            return messageDao.save(message);
        }
        return message;
    }

    /**
     * Retrieves all messages from the database
     * @return a list of all messages if any
     */
    public List<Message> getAllMessages() {
        return messageDao.getAll();
    }

    /**
     * Retrieves a message with the given message id from the database.
     * @param id the message id
     * @return the message if it exists
     */
    public Message getMessageById(int id) {
        return messageDao.getMessageById(id);
    }

    /**
     * Deletes the message with the given id from the database.
     * @param id the id of the message to be deleted
     */
    public void deleteMessage(int id) {
        messageDao.remove(id);
    }

    /**
     * To update the text of the message with the given message id
     * @param id the id of the message to be updated
     * @param message_text the new message text
     * @return the updated message if successful, the old message otherwise
     */
    public Message updateMessageText(int id, String message_text) {
        Message message = new Message();
        if (validateMessageText(message_text)) {
            message = getMessageById(id);
            
            if (message.getMessage_id() > 0) {
                message.setMessage_text(message_text);
                messageDao.update(message);   
            }
        }
        return message;
    }

    /**
     * To validate a message object
     * @param message the message to be validated
     * @return true if the message meets all requirements, false otherwise.
     */
    private boolean validateMessage(Message message) {
        String text = message.getMessage_text();
        if (accountDao.getAccountById(message.getPosted_by()) == null) {
            return false;
        }
        return validateMessageText(text);
    }

    /**
     * To validate message text
     * @param text the message text to be validated
     * @return true if the text meets all requirements false otherwise.
     */
    private boolean validateMessageText(String text) {
        if (text.trim().isEmpty() || text.trim().length() > 255) {
            return false;
        }
        return true;
    }

    /**
     * To retrieve all messages posted by the account with the given id
     * @param posted_by the account id of the messages poster
     * @return A list of all messages posted by the account, an empty list otherwise
     */
    public List<Message> getUserMessages(int posted_by) {
        return messageDao.getMessagesByUser(posted_by);
    }
}
