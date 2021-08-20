package javapro.services;

import javapro.api.request.CreateDialogRequest;
import javapro.api.response.*;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.model.*;
import javapro.model.dto.DialogMessageDTO;
import javapro.model.enums.ReadStatus;
import javapro.repository.Dialog2PersonRepository;
import javapro.repository.DialogMessageRepository;
import javapro.repository.DialogRepository;
import javapro.repository.PersonRepository;
import javapro.util.Time;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DialogsService {

    private final DialogRepository dialogRepository;
    private final PersonRepository personRepository;
    private final DialogMessageRepository dialogMessageRepository;
    private final Dialog2PersonRepository dialog2PersonRepository;


    public DialogsService(DialogRepository dialogRepository, PersonRepository personRepository, DialogMessageRepository dialogMessageRepository, Dialog2PersonRepository dialog2PersonRepository) {
        this.dialogRepository = dialogRepository;
        this.personRepository = personRepository;
        this.dialogMessageRepository = dialogMessageRepository;
        this.dialog2PersonRepository = dialog2PersonRepository;
    }

    public ResponseEntity<AllPersonDialogsResponse> getAllPersonDialogs(String personEmail, Integer offset, Integer perPage) {
        List<DialogData> dialogData;
        var person = personRepository.findByEmail(personEmail);
        Pageable pageWithDialogs = PageRequest.of(offset / perPage, perPage);
        //получение списка диалогов пользователя
        List<Dialog> allUserDialog = Optional.of(dialogRepository.findAllPersonDialogs(pageWithDialogs, person.getId()))
                .orElse(new ArrayList<>());
        var countDialogs = dialogRepository.personDialogsCount(person.getId());
        dialogData = prepareDialogData(allUserDialog, person.getId());
        return new ResponseEntity<>(prepareAllPersonDialogResponse(dialogData,
                countDialogs, offset, perPage), HttpStatus.OK);
    }

    public ResponseEntity<DialogMessagesResponse> getDialogMessages(Integer dialogId, Integer offset, Integer perPage, String currentUserEmail) throws BadRequestException {
        List<DialogMessageData> dialogMessageDataList;
        var dialog = dialogRepository.findById(dialogId).orElseThrow(() -> new BadRequestException("dialog not found"));
        List<DialogMessage> allDialogMessages = dialog.getDialogMessageList();
        List <DialogMessage> dialogMessagePage = allDialogMessages.stream()
                                                                  .sorted()
                                                                  .skip(offset / (perPage - 1) * (perPage - 1))
                                                                  .limit(perPage)
                                                                  .collect(Collectors.toList());
        for (DialogMessage dialogMessage : dialogMessagePage) {
            if (dialogMessage.getRecipientId().getEmail().equals(currentUserEmail)) {
                dialogMessage.setReadStatus(ReadStatus.READ);
                dialogMessageRepository.save(dialogMessage);
            }
        }
        dialogMessageDataList = prepareDialogMessageData(dialogMessagePage, currentUserEmail);
        return new ResponseEntity<>(prepareDialogMessageResponse(dialogMessageDataList,
                allDialogMessages.size(), offset, perPage), HttpStatus.OK);
    }

    public ResponseEntity<UnreadedCountResponse> getUnreadCount(String personEmail){
        var unreadCount = 0;
        var unreadedCountResponse = new UnreadedCountResponse();
        var person = personRepository.findByEmail(personEmail);
        List<Dialog> allPersonDialogs = dialogRepository.findAllPersonDialogs(person.getId());
        if (allPersonDialogs != null) {
            for (Dialog dialog : allPersonDialogs) {
                unreadCount += getUnreadCountMessageInDialog(dialog, person.getId());
            }
            unreadedCountResponse = new UnreadedCountResponse(
                    Config.ERROR_MESSAGE,
                    Time.getTime(),
                    new UnreadedCountData(unreadCount));
        }
        return new ResponseEntity<>(unreadedCountResponse, HttpStatus.OK);
    }

    public ResponseEntity<AddDialogMessageResponse> addDialogMessage(Integer id, String message, String authorEmail) throws BadRequestException {
        var author = personRepository.findByEmail(authorEmail);
        var dialog = dialogRepository.findById(id).orElseThrow(() -> new BadRequestException("dialog not found"));
        var newMessage = new DialogMessage();
        var recipient = dialog.getPersonInDialog().stream()
                                                     .filter(p -> !p.getId().equals(author.getId()))
                                                     .findFirst().orElseThrow(() -> new BadRequestException("Recipient not found"));
        newMessage.setTime(new Date());
        newMessage.setDialog(dialog);
        newMessage.setAuthorId(author);
        newMessage.setRecipientId(recipient);
        newMessage.setMessageText(message);
        newMessage.setReadStatus(ReadStatus.SENT);
        dialogMessageRepository.save(newMessage);
        return new ResponseEntity<>(prepareAddDialogMessageResponse(author, recipient, newMessage), HttpStatus.OK);
    }

    public ResponseEntity<CreateDialogResponse> createDialog(CreateDialogRequest createDialogRequest, String currentUserEmail){
        Person currentPerson = personRepository.findByEmail(currentUserEmail);
        Person addingPerson = personRepository.findPersonById(createDialogRequest.getUsersId().get(0));
        List<Dialog> dialogList = currentPerson.getPersonsDialogs();
        if (dialogList != null) {
            for (Dialog dialog : dialogList) {
                Dialog2person dialog2person = dialog2PersonRepository.findByDialogIdAndPersonId(dialog.getId(), addingPerson.getId());
                if (dialog2person != null) {
                    return new ResponseEntity<>(prepareCreateDialogResponse(dialog2person.getDialog().getId()), HttpStatus.OK);
                }
            }
        }
        Dialog dialog = new Dialog();
        dialogRepository.save(dialog);
        addInDialog2Person(dialog, currentPerson);
        addInDialog2Person(dialog, addingPerson);
        DialogMessage newMessage = new DialogMessage();
        newMessage.setTime(new Date());
        newMessage.setDialog(dialog);
        newMessage.setAuthorId(currentPerson);
        newMessage.setRecipientId(addingPerson);
        newMessage.setMessageText(String.format("Create new dialog with %s %s!", currentPerson.getFirstName(), currentPerson.getLastName()));
        newMessage.setReadStatus(ReadStatus.SENT);
        dialogMessageRepository.save(newMessage);
        return new ResponseEntity<>(prepareCreateDialogResponse(dialog.getId()), HttpStatus.OK);
    }

    private void addInDialog2Person(Dialog dialog, Person person){
        Dialog2person dialog2person = new Dialog2person();
        dialog2person.setDialog(dialog);
        dialog2person.setPerson(person);
        dialog2PersonRepository.save(dialog2person);
    }

    private Integer getUnreadCountMessageInDialog(Dialog dialog, Integer personId){
        return (int) dialog.getDialogMessageList().stream()
                                     .filter(dialogMessage -> ((!dialogMessage.getAuthorId().getId().equals(personId)) && (dialogMessage.getReadStatus() != ReadStatus.READ)))
                                     .count();
    }

    private CreateDialogResponse prepareCreateDialogResponse(Integer dialogId) {
        CreateDialogResponse createDialogResponse = new CreateDialogResponse();
        createDialogResponse.setError(Config.ERROR_MESSAGE);
        createDialogResponse.setTimestamp(Time.getTime());
        createDialogResponse.setData(new CreateDialogData(dialogId));
        return createDialogResponse;
    }


    private AllPersonDialogsResponse prepareAllPersonDialogResponse(List<DialogData> dialogDataList,
                                                                    Integer total, Integer offset, Integer perPage){
        AllPersonDialogsResponse allPersonDialogsResponse = new AllPersonDialogsResponse();
        allPersonDialogsResponse.setError(Config.ERROR_MESSAGE);
        allPersonDialogsResponse.setTimestamp(Time.getTime());
        allPersonDialogsResponse.setTotal(total);
        allPersonDialogsResponse.setOffset(offset);
        allPersonDialogsResponse.setPerPage(perPage);
        allPersonDialogsResponse.setData(dialogDataList);
        return allPersonDialogsResponse;
    }

    private DialogMessagesResponse prepareDialogMessageResponse(List<DialogMessageData> dialogMessageDataList,
                                                                Integer total, Integer offset, Integer perPage){
        DialogMessagesResponse dialogMessagesResponse = new DialogMessagesResponse();
        dialogMessagesResponse.setError(Config.ERROR_MESSAGE);
        dialogMessagesResponse.setTimestamp(Time.getTime());
        dialogMessagesResponse.setTotal(total);
        dialogMessagesResponse.setOffset(offset);
        dialogMessagesResponse.setPerPage(perPage);
        dialogMessagesResponse.setData(dialogMessageDataList);
        return dialogMessagesResponse;
    }

    private List<DialogData> prepareDialogData(List<Dialog> allUserDialog, Integer personId){
        List<DialogData> dialogDataList = new ArrayList<>();
        List<DialogMessage> dialogMessages;
        for (Dialog dialog : allUserDialog) {
            dialogMessages = dialog.getDialogMessageList();
            if (!dialogMessages.isEmpty()) {
                Collections.sort(dialogMessages);
                var lastDialogMessage = dialogMessages.get(dialogMessages.size() - 1);
                dialogDataList.add(new DialogData(dialog.getId(), getUnreadCountMessageInDialog(dialog, personId),
                        new DialogMessageDTO(lastDialogMessage.getId(),
                                lastDialogMessage.getTime().getTime(),
                                lastDialogMessage.getAuthorId().getId(),
                                prepareRecipientData(lastDialogMessage.getRecipientId()),
                                lastDialogMessage.getMessageText(),
                                lastDialogMessage.getReadStatus())));
            }
        }
        Collections.sort(dialogDataList);
        return dialogDataList;
    }

    private List<DialogMessageData> prepareDialogMessageData(List<DialogMessage> dialogMessageList, String currentUserEmail){
        List<DialogMessageData> dialogMessageDataList = new ArrayList<>();
        for (DialogMessage dialogMessage : dialogMessageList) {
            dialogMessageDataList.add(new DialogMessageData(dialogMessage.getId(),
                                                            dialogMessage.getAuthorId().getId(),
                                                            prepareRecipientData(dialogMessage.getRecipientId()),
                                                            dialogMessage.getMessageText(),
                                                            dialogMessage.getReadStatus(),
                                                            dialogMessage.getAuthorId().getEmail().equals(currentUserEmail)));
        }
        return dialogMessageDataList;
    }

    private AddDialogMessageResponse prepareAddDialogMessageResponse(Person author, Person recipient, DialogMessage newMessage){
        var addDialogMessageResponse = new AddDialogMessageResponse();
        var addDialogMessageData= new AddDialogMessageData();
        addDialogMessageData.setId(newMessage.getId());
        addDialogMessageData.setTime(newMessage.getTime().getTime());
        addDialogMessageData.setAuthorId(author.getId());
        addDialogMessageData.setRecipientId(recipient.getId());
        addDialogMessageData.setMessageText(newMessage.getMessageText());
        addDialogMessageData.setReadStatus(newMessage.getReadStatus());
        addDialogMessageResponse.setError(Config.ERROR_MESSAGE);
        addDialogMessageResponse.setTimestamp(Time.getTime());
        addDialogMessageResponse.setData(addDialogMessageData);
        return addDialogMessageResponse;
    }

    private RecipientData prepareRecipientData(Person recipient){
        RecipientData recipientData = new RecipientData();
        recipientData.setId(recipient.getId());
        recipientData.setFirstName(recipient.getFirstName());
        recipientData.setLastName(recipient.getLastName());
        recipientData.setLastOnlineTime(recipient.getLastOnlineTime().getTime());
        recipientData.setPhoto(recipient.getPhoto());
        return recipientData;
    }
}
