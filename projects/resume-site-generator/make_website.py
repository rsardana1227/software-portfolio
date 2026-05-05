def surround_block(tag, text):
    """
    Surrounds the given text with the given html tag and returns the string.
    """
    return f"<{tag}>{text}</{tag}>"

def create_email_link(email_address):
    """
    Creates an email link with the given email_address.
    To cut down on spammers harvesting the email address from the webpage,
    displays the email address with [aT] instead of @.

    Example: Given the email address: lbrandon@wharton.upenn.edu
    Generates the email link: <a href="mailto:lbrandon@wharton.upenn.edu">lbrandon[aT]wharton.upenn.edu</a>

    Note: If, for some reason the email address does not contain @,
    use the email address as is and don't replace anything.
    """
    display_email = email_address.replace('@', '[aT]', 1) if '@' in email_address else email_address
    return f'<a href="mailto:{email_address}">{display_email}</a>'

def generate_html(txt_input_file, html_output_file):
    """
    Loads given txt_input_file,
    gets the name, email address, list of projects, and list of courses,
    then writes the info to the given html_output_file.

    # Hint(s):
    # call function(s) to load given txt_input_file
    # call function(s) to get name
    # call function(s) to get email address
    # call function(s) to get list of projects
    # call function(s) to get list of courses
    # call function(s) to write the name, email address, list of projects, and list of courses to the given html_output_file
    """
    with open(txt_input_file, 'r', encoding='utf-8') as infile:
        lines = [line.rstrip('\n') for line in infile]

    name = lines[0].strip() if lines else "Candidate"
    courses = []
    projects = []
    email = ""
    in_projects = False

    for raw_line in lines[1:]:
        line = raw_line.strip()
        if not line:
            continue
        if line.startswith('Courses'):
            _, value = line.split(':-', 1)
            courses = [course.strip() for course in value.split(',') if course.strip()]
        elif line == 'Projects':
            in_projects = True
        elif line.startswith('---'):
            in_projects = False
        elif '@' in line and ' ' not in line:
            email = line
        elif in_projects:
            projects.append(line)

    body = [
        '<div id="page-wrap">',
        surround_block('h1', name),
        surround_block('p', create_email_link(email)),
    ]

    if projects:
        body.append(surround_block('h2', 'Projects'))
        body.append('<ul>')
        for project in projects:
            body.append(surround_block('li', project))
        body.append('</ul>')

    if courses:
        body.append(surround_block('h2', 'Courses'))
        body.append('<ul>')
        for course in courses:
            body.append(surround_block('li', course))
        body.append('</ul>')

    body.extend(['</div>'])

    html = """<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>Resume</title>
</head>
<body>
""" + "\n".join(body) + """
</body>
</html>
"""

    with open(html_output_file, 'w', encoding='utf-8') as outfile:
        outfile.write(html)

def main():

    # DO NOT REMOVE OR UPDATE THIS CODE
    # generate resume.html file from provided sample resume.txt
    generate_html('resume.txt', 'resume.html')

    # DO NOT REMOVE OR UPDATE THIS CODE.
    # Uncomment each call to the generate_html function when you’re ready
    # to test how your program handles each additional test resume.txt file
    #generate_html('TestResumes/resume_bad_name_lowercase/resume.txt', 'TestResumes/resume_bad_name_lowercase/resume.html')
    #generate_html('TestResumes/resume_courses_w_whitespace/resume.txt', 'TestResumes/resume_courses_w_whitespace/resume.html')
    #generate_html('TestResumes/resume_courses_weird_punc/resume.txt', 'TestResumes/resume_courses_weird_punc/resume.html')
    #generate_html('TestResumes/resume_projects_w_whitespace/resume.txt', 'TestResumes/resume_projects_w_whitespace/resume.html')
    #generate_html('TestResumes/resume_projects_with_blanks/resume.txt', 'TestResumes/resume_projects_with_blanks/resume.html')
    #generate_html('TestResumes/resume_template_email_w_whitespace/resume.txt', 'TestResumes/resume_template_email_w_whitespace/resume.html')
    #generate_html('TestResumes/resume_wrong_email/resume.txt', 'TestResumes/resume_wrong_email/resume.html')

    # If you want to test additional resume files, call the generate_html function with the given .txt file
    # and desired name of output .html file

if __name__ == '__main__':
    main()
