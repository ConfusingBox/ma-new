package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventplanner.R;

public class UserDetailsFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private int userId;

    private LinearLayout chatContainer;
    private EditText messageInput;

    public UserDetailsFragment() {
        // Required empty public constructor
    }

    // ✅ OVO JE ISPRAVNA VERZIJA newInstance — nalazi se unutar klase
    public static UserDetailsFragment newInstance(int userId) {
        UserDetailsFragment fragment = new UserDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_details, container, false);

        chatContainer = view.findViewById(R.id.chatContainer);
        messageInput = view.findViewById(R.id.messageInput);
        Button sendButton = view.findViewById(R.id.sendMessageButton);
        Button blockButton = view.findViewById(R.id.blockUserButton);
        Button reportButton = view.findViewById(R.id.reportUserButton);

        // Dummy chat poruke (radi prikaza)
        addChatMessage("Pozdrav", false);
        addChatMessage("Dobar dan", true);
        addChatMessage("Pogledao sam vašu ponudu", false);
        addChatMessage("Zanima me jedan od vaših proizvoda", false);

        // Dugme za slanje nove poruke
        sendButton.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (!message.isEmpty()) {
                addChatMessage(message, true);
                messageInput.setText("");
            }
        });

        // Dummy akcije za Block i Report
        blockButton.setOnClickListener(v ->
                android.widget.Toast.makeText(getContext(), "User blocked", android.widget.Toast.LENGTH_SHORT).show());

        reportButton.setOnClickListener(v ->
                android.widget.Toast.makeText(getContext(), "User reported", android.widget.Toast.LENGTH_SHORT).show());

        return view;
    }

    /**
     * Dodaje poruku u chat kontejner
     * @param message Tekst poruke
     * @param isSentByMe Ako je true, poruka ide desno
     */
    private void addChatMessage(String message, boolean isSentByMe) {
        TextView textView = new TextView(getContext());
        textView.setText(message);
        textView.setTextSize(16);
        textView.setPadding(16, 8, 16, 8);
        textView.setBackgroundResource(isSentByMe ? R.drawable.chat_bubble_sent : R.drawable.chat_bubble_received);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(8, 8, 8, 8);
        params.gravity = isSentByMe ? android.view.Gravity.END : android.view.Gravity.START;
        textView.setLayoutParams(params);

        chatContainer.addView(textView);
    }
}
