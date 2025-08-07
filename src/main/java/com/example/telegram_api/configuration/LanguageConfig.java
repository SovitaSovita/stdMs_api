package com.example.telegram_api.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class LanguageConfig {
    public static final String PRIVACY_KH = """
            សូមស្វាគមន៍មកកាន់ / Welcome\s

            សមាគមអ្នកវិនិយោគមូលបត្រកម្ពុជា- Securities Investors Association of Cambodia\s

            កាតព្វកិច្ចដេីម្បីចូលជាសមាជិកសមាគមពេញសិទ្ធ :\s
            (Obligation to become member )

            1. មានចំណូលច្រេីនជាងចំណាយ ។
            (Income>Expense)

            2. នាំសមាជិកថ្មីពីរនាក់ក្នុងរយះពេល 3 ទៅ 6ខែ បន្ទាប់ពីបានចំណេះដឹងហេីយ ។\s
            (Refer 2 new members in 3 to 6months)

            3. ឧបត្ថម្ភសមាគម 1ឆ្នាំ 50ដុល្លារ ។\s
            (Contribute 50usd  yearly)

            4. មានគំនិតវិជ្ជាមានសំរាប់ខ្លួនឯង សមាគម និងប្រទេសជាតិ ។\s
            (Be Positive)

            5. យល់ព្រមចូលរៀនវគ្គ មូលដ្ឋានគ្រឹះ និងប្រលងអោយជាប់ ដេីម្បីក្លាយជាសមាជិករបស់សមាគម 1ឆ្នាំម្តងយ៉ាងតិច ។\s
            (Commit to Learn and Pass Exam)\s

            6.គោរពគោលការណ៍ និង លក្ខន្តិកៈរបស់សមាគម ។\s
            (Respect Rules and Regulations)\s

            យល់ព្រម និងចុះឈ្មោះ
            /Confirm & Register
            
            """;

    public static final String WELCOME_KH = """
            សូមស្វាគមន៍មកកាន់ សមាគមអ្នកវិនិយោគមូលបត្រកម្ពុជា
            Welcome to the Securities Investors Association of Cambodia (SIAC) BOT!
            """;

    public static final String YOU_ALREADY_REGISTERED = """
            <b>
            អ្នកបានចុះឈ្មោះជាសមាជិករួចហើយ។
            You already registered an account.
            </b>
            -បញ្ចូល '/view' ដើម្បីបង្ហាញព័ត៌មានគណនីដែលមានស្រាប់
            - Enter '/view' to display existing account information. 👈
            """;

    public static final String NOT_MEMBER = """
            អ្នកមិនទាន់ចុះឈ្មោះជាសមាជិក SIAC នៅឡើយទេ
            You are not SIAC member yet.

            Please Enter /register to register as SIAC member.
            """;

    public static final String INPUT_EMAIL_KH = """
            🔸សូមបញ្ចូលអុីម៉ែល
            🔸Please Input your Email

            Ex: example@gmail.com""";

    public static final String INPUT_SPONSOR_ID_KH = """
            🔸សូមបញ្ចូលលេខកូដណែនាំរបស់អ្នក
            🔸Please input your sponsor’s ID:
            
            """;

    public static final String ASK_REGISTER_WITH_CODE = """
                        🔸តើអ្នកនឹងចុះឈ្មោះដោយប្រើលេខកូដណែនាំទេ?
                        🔸Do you want to register with your sponsor's ID?
                        
                        """;

    public static final String INVALID_NUMBER_KH = """
            📍Invalid code format. Please enter a valid numerical code.
            """;

    public static final String INVALID_EMAIL_KH = """
            📍អុីម៉ែលរបស់អ្នកមិនត្រឹមត្រូវ
            📍Invalid email. Please try the correct one.
            """;

    public static final String INVALID_FULL_NAME_KH = """
            📍 ឈ្មោះពេញរបស់អ្នកមិនត្រឹមត្រូវ
            📍 Your full name is Invalid.
            """;

    public static String INPUT_PH_KH = """
            🔸សូមបញ្ចូលលេខទូរសព្ទ័ប្រេីតេលេក្រាម
            🔸Please enter your Telegram phone number.
            
            """;

    public static final String INPUT_FULLNAME_KH = """
            🔸សូមបញ្ចូលឈ្មោះពេញ
            🔸Please Input Your full name
            
            """;

    public static final String THANK_U_KH = """
            សូមអរគុណសម្រាប់ការចុះឈ្មោះ! យើងនឹងពិនិត្យមើលសំណើរបស់អ្នក ហើយឆ្លើយតបអោយបានលឿនបំផុត។
            Thank you for registering! We will review your request and respond at the earliest opportunity.🙏
            """;

    public static final String YOUR_MEMBERSHIP_HAS_EXPIRED_KH = """
            membership របស់អ្នកបានផុតកំណត់ហើយ។ យើងសូមស្នើឱ្យអ្នកបន្តប្រតិបត្តិការរបស់អ្នកឡើងវិញ។
            Your membership has expired. We kindly request you to renew your transaction, please.
            
            - បញ្ចូល '/transaction' ដើម្បី upload ប្រតិបត្តិការរបស់អ្នកឡើងវិញ
            - Enter '/transaction' to upload/re-new your transaction.
            """;
}
