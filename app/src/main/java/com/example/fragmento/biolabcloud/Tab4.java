package com.example.fragmento.biolabcloud;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;

import java.util.ArrayList;
import java.util.List;


public class Tab4 extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "Tab4";
    private ConversationService myConversationService = null;
    private RecyclerView chatDisplayTV;
    private EditText userStatementET;
    List<ReponseMessage> responseMessagesList;
    MessageAdapter messageAdapter;
    private final String IBM_USERNAME = "f9e374d0-fa06-4d80-9467-6f83d8e5f116";
    private final String IBM_PASSWORD = "1YIZ2CaWXU02";
    private final String IBM_WORKSPACE_ID = "beeaf619-87a3-4ee3-97b8-e71265877f30";


    public Tab4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_tab4, container, false);
        chatDisplayTV = v.findViewById(R.id.tv_chat_display);
        userStatementET = v.findViewById(R.id.et_user_statement);
        responseMessagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(responseMessagesList);
        chatDisplayTV.setLayoutManager(new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false));
        chatDisplayTV.setAdapter(messageAdapter);
        //instantiating IBM Watson Conversation Service
        myConversationService =
                new ConversationService(
                        "2017-12-06",
                        IBM_USERNAME,
                        IBM_PASSWORD
                );

        userStatementET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    final String userStatement = userStatementET.getText().toString();


                    ReponseMessage message = new ReponseMessage(userStatement.toString(),true);
                    responseMessagesList.add(message);
                    messageAdapter.notifyDataSetChanged();

                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(userStatement)
                            .build();

                    myConversationService
                            .message(IBM_WORKSPACE_ID, request)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    final String botStatement = response.getText().get(0);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ReponseMessage message2 = new ReponseMessage(botStatement.toString(),false);
                                            responseMessagesList.add(message2);
                                            messageAdapter.notifyDataSetChanged();
                                        }
                                    });


                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            });


                    messageAdapter.notifyDataSetChanged();
                    userStatementET.setText("");
                    if(!isVisibles()){
                        chatDisplayTV.smoothScrollToPosition(messageAdapter.getItemCount());
                    }
                }
                return true;
            }
        });


       /* userStatementET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int action, KeyEvent keyEvent) {
                if (action == EditorInfo.IME_ACTION_DONE) {
                    //show the user statement
                    final String userStatement = userStatementET.getText().toString();
                    chatDisplayTV.append(
                            Html.fromHtml("<p><b>TU:</b> " + userStatement + "</p>")
                    );
                    userStatementET.setText("");

                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(userStatement)
                            .build();
                    // initiate chat conversation
                    myConversationService
                            .message(IBM_WORKSPACE_ID, request)
                            .enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    final String botStatement = response.getText().get(0);
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            chatDisplayTV.append(
                                                    Html.fromHtml("<p><b>Ro:</b> " +
                                                            botStatement + "</p>")
                                            );
                                        }
                                    });


                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Log.d(TAG, e.getMessage());
                                }
                            });
                }
                return false;
            }
        });*/



       return v;

    }

    public boolean isVisibles(){
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) chatDisplayTV.getLayoutManager();
        int positionOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemCount = chatDisplayTV.getAdapter().getItemCount();
        return (positionOfLastVisibleItem>=itemCount);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
