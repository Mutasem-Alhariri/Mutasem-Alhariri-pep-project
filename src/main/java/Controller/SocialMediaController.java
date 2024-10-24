package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private final AccountService accountAervice = new AccountService();
    private final MessageService messageService = new MessageService();
    private ObjectMapper om = new ObjectMapper();
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::register);
        app.post("login", this::login);
        app.post("messages", this::saveMessage);
        app.get("messages", this::getAllMessages);
        app.get("messages/{message_id}", this::getMessage);
        app.delete("messages/{message_id}", this::deleteMessage);
        app.patch("messages/{message_id}", this::updateMessageText);
        app.get("accounts/{account_id}/messages", this::getUserMessages);
        return app;
    }

    /**Post("/register")
     * a handler to register a new user account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void register(Context context) {
        try {
            Account account = om.readValue(context.body(), Account.class);
            account = accountAervice.createAccount(account);
            if(account.getAccount_id() > 0) {
                context.json(account);
            }
            else {
                context.status(400);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * post("/login")
     * A handler to authenticate users.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void login(Context context) {
        try {
            Account account = om.readValue(context.body(), Account.class);
            account = accountAervice.login(account);
            if(account.getAccount_id() > 0) {
                context.json(account);
            }
            else {
                context.status(401);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * post("/messages")
     * A handler to save a new message.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void saveMessage(Context context) {
        try {
            Message message = om.readValue(context.body(), Message.class);
            message = messageService.createMessage(message) ;
            if(message.getMessage_id() > 0) {
                context.json(message);
            }
            else {
                context.status(400);
            }
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get("/messages")
     * A handler to retrieve all messages.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getAllMessages(Context context) {
        context.json(messageService.getAllMessages());
    }

    /**
     * Get("messages/{message_id}")
     * A handler to retrieve a message with the given message_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getMessage(Context context) {
        try {
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(message_id);
            if(message.getMessage_id() > 0) {
                context.json(message);
            }
            else {
                context.result("");
            }
        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID: " + context.pathParam("message_id"));
        }
    }

    /**
     * delete("messages/{message_id}")
     * A handler to delete a message with the given message_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void deleteMessage(Context context) {
        try {
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            Message message = messageService.getMessageById(message_id);
            if(message.getMessage_id() > 0) {
                messageService.deleteMessage(message_id);
                context.json(message);
            }
            else {
                context.result("");
            }
        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID: " + context.pathParam("message_id"));
        }
    }

    /**
     * patch("messages/{message_id}")
     * A handler for updating the text of the message with the given message_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void updateMessageText(Context context) {
        try {
            int message_id = Integer.parseInt(context.pathParam("message_id"));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode;
            String message_text = "";
            try {
                jsonNode = mapper.readTree(context.body());
                message_text = jsonNode.get("message_text").asText();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            Message message = messageService.updateMessageText(message_id, message_text);
            if(message.getMessage_id() > 0) {
                context.json(message);
            }
            else {
                context.status(400);
            }
        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID: " + context.pathParam("message_id"));
        }
    }

    /**
     * get("accounts/{account_id/messages}")
     * A handler for retrieving all messages posted by the account wit the given account_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void getUserMessages(Context context) {
        try {
            int posted_by = Integer.parseInt(context.pathParam("account_id"));
            context.json(messageService.getUserMessages(posted_by));
        } catch (NumberFormatException e) {
            context.status(400).json("Invalid message ID: " + context.pathParam("message_id"));
        }
    }
}