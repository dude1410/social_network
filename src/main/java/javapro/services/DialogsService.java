package javapro.services;

import javapro.api.request.CreateDialogRequest;
import javapro.api.response.*;
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
        Person person = personRepository.findByEmail(personEmail);
        Pageable pageWithDialogs = PageRequest.of(offset / perPage, perPage);
        //получение списка диалогов пользователя
        List<Dialog> allUserDialog = Optional.of(dialogRepository.findAllPersonDialogs(pageWithDialogs, person.getId()))
                .orElse(new ArrayList<>());
        dialogData = prepareDialogData(allUserDialog, person.getId());
        return new ResponseEntity<>(prepareAllPersonDialogResponse(dialogData,
                allUserDialog.size(), offset, perPage), HttpStatus.OK);
    }

    public ResponseEntity<DialogMessagesResponse> getDialogMessages(Integer dialogId, Integer offset, Integer perPage) throws BadRequestException {
        List<DialogMessageData> dialogMessageDataList;
        Dialog dialog = dialogRepository.findById(dialogId).orElseThrow(() -> new BadRequestException("dialog not found"));
        List<DialogMessage> allDialogMessages = dialog.getDialogMessageList();
        List <DialogMessage> dialogMessagePage = allDialogMessages.stream()
                                                                  .sorted()
                                                                  .skip(offset)
                                                                  .limit(perPage)
                                                                  .collect(Collectors.toList());
        dialogMessageDataList = prepareDialogMessageData(dialogMessagePage);
        return new ResponseEntity<>(prepareDialogMessageResponse(dialogMessageDataList,
                allDialogMessages.size(), offset, perPage), HttpStatus.OK);
    }

    public ResponseEntity<UnreadedCountResponse> getUnreadCount(String personEmail){
        int unreadCount = 0;
        UnreadedCountResponse unreadedCountResponse = new UnreadedCountResponse();
        Person person = personRepository.findByEmail(personEmail);
        List<Dialog> allPersonDialogs = dialogRepository.findAllPersonDialogs(person.getId());
        if (allPersonDialogs != null) {
            for (Dialog dialog : allPersonDialogs) {
                unreadCount += getUnreadCountMessageInDialog(dialog, person.getId());
            }
            unreadedCountResponse = new UnreadedCountResponse(
                    "string",
                    Time.getTime(),
                    new UnreadedCountData(unreadCount));
        }
        return new ResponseEntity<>(unreadedCountResponse, HttpStatus.OK);
    }

    public ResponseEntity<AddDialogMessageResponse> addDialogMessage(Integer id, String message, String authorEmail) throws BadRequestException {
        Person author = personRepository.findByEmail(authorEmail);
        System.out.println("dialog id " + id);
        Dialog dialog = dialogRepository.findById(id).orElseThrow(() -> new BadRequestException("dialog not found"));
        DialogMessage newMessage = new DialogMessage();
        System.out.println(dialog.getPersonInDialog().get(0).getEmail() + "======" + dialog.getPersonInDialog().get(1).getEmail());
        Person recipient = dialog.getPersonInDialog().stream()
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
        Dialog dialog = new Dialog();
        dialogRepository.save(dialog);
        addInDialog2Person(dialog, currentPerson);
        addInDialog2Person(dialog, addingPerson);
        CreateDialogResponse createDialogResponse = new CreateDialogResponse();
        createDialogResponse.setError("string");
        createDialogResponse.setTimestamp(Time.getTime());
        createDialogResponse.setData(new CreateDialogData(dialog.getId()));
        return new ResponseEntity<>(createDialogResponse, HttpStatus.OK);
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


    private AllPersonDialogsResponse prepareAllPersonDialogResponse(List<DialogData> dialogDataList,
                                                                    Integer total, Integer offset, Integer perPage){
        AllPersonDialogsResponse allPersonDialogsResponse = new AllPersonDialogsResponse();
        allPersonDialogsResponse.setError("string");
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
        dialogMessagesResponse.setError("string");
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
            if (dialogMessages.size() != 0) {
                Collections.sort(dialogMessages);
                DialogMessage lastDialogMessage = dialogMessages.get(dialogMessages.size() - 1);
                RecipientData recipientData = new RecipientData();
                recipientData.setId(lastDialogMessage.getRecipientId().getId());
                recipientData.setFirstName(lastDialogMessage.getRecipientId().getFirstName());
                recipientData.setLastName(lastDialogMessage.getRecipientId().getLastName());
                recipientData.setLastOnlineTime(lastDialogMessage.getRecipientId().getLastOnlineTime().getTime());
                dialogDataList.add(new DialogData(dialog.getId(), getUnreadCountMessageInDialog(dialog, personId),
                        new DialogMessageDTO(lastDialogMessage.getId(),
                                lastDialogMessage.getTime().getTime(),
                                lastDialogMessage.getAuthorId().getId(),
                                recipientData,
                                lastDialogMessage.getMessageText(),
                                lastDialogMessage.getReadStatus())));
            }
        }
        return dialogDataList;
    }

    private List<DialogMessageData> prepareDialogMessageData(List<DialogMessage> dialogMessageList){
        List<DialogMessageData> dialogMessageDataList = new ArrayList<>();

        for (DialogMessage dialogMessage : dialogMessageList) {
            RecipientData recipientData = new RecipientData();
            recipientData.setId(dialogMessage.getRecipientId().getId());
            recipientData.setFirstName(dialogMessage.getRecipientId().getFirstName());
            recipientData.setLastName(dialogMessage.getRecipientId().getLastName());
            recipientData.setLastOnlineTime(dialogMessage.getRecipientId().getLastOnlineTime().getTime());
            dialogMessageDataList.add(new DialogMessageData(dialogMessage.getId(),
                    dialogMessage.getAuthorId().getId(),
                    recipientData,
                    dialogMessage.getMessageText(),
                    dialogMessage.getReadStatus()));
        }
        return dialogMessageDataList;
    }

    private AddDialogMessageResponse prepareAddDialogMessageResponse(Person author, Person recipient, DialogMessage newMessage){
        AddDialogMessageResponse addDialogMessageResponse = new AddDialogMessageResponse();
        AddDialogMessageData addDialogMessageData= new AddDialogMessageData();
        addDialogMessageData.setId(newMessage.getId());
        addDialogMessageData.setTime(newMessage.getTime().getTime());
        addDialogMessageData.setAuthorId(author.getId());
        addDialogMessageData.setRecipientId(recipient.getId());
        addDialogMessageData.setMessageText(newMessage.getMessageText());
        addDialogMessageData.setReadStatus(newMessage.getReadStatus());
        addDialogMessageResponse.setError("string");
        addDialogMessageResponse.setTimestamp(Time.getTime());
        addDialogMessageResponse.setData(addDialogMessageData);
        return addDialogMessageResponse;
    }
}
